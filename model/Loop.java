package model;

import model.events.EventManager;
import model.events.InputListener;
import view.Displayer;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.function.Supplier;

import controller.Controller;
import model.cards.Card;
import model.cards.Deck;

public class Loop implements InputListener {
    /* SINGLETON */
    /* ------------------------------ */
    private static Loop instance;

    public static Loop getInstance() {
        if (instance == null)
            instance = new Loop();
        return instance;
    }

    private Loop() {
        choiceTypes = new HashMap<>();
        events = new EventManager();

        choiceTypes.put("card", () -> {
            Card c = (Card) choice;
            if (Actions.tryChangeCard(c)) {
                player.hand.remove(c);
                c.getEffect().ifPresent(e -> e.cast());
                events.notify("cardPlayed", c);

                if (player.getHand().size() == 1 && !unoDeclared) {
                    Actions.dealFromDeck(player, 1);
                    events.notify("playerDrew", player);
                }

                events.notify("playerHandChanged", player);
                return true;
            } else {
                events.notify("warning", "Can't play it now!");
                return false;
            }
        });
        choiceTypes.put("draw", () -> {
            Actions.dealFromDeck(player);
            events.notify("playerDrew", player);
            return true;
        });
        choiceTypes.put("unoDeclared", () -> {
            startUnoTimer();
            return false;
        });
        choiceTypes.put("cardPosition", () -> {
            try {
                choice = player.getHand().get((int) choice);
                return choiceTypes.get("card").get();
            } catch (IndexOutOfBoundsException e) {
                events.notify("warning", "Invalid selection!");
                return false;
            }
        });
    }

    /* ------------------------------ */

    public EventManager events;
    public HashMap<String, Supplier<Boolean>> choiceTypes;
    private Displayer disp;

    private Game g;
    private Player player;
    private Object choice;
    private String choiceType;

    private Controller[] users;

    private boolean unoDeclared;

    public void setupView(Displayer displayer) {
        disp = displayer;
        for (String event : disp.getEventsListening())
            events.subscribe(event, disp);
    }

    public void setupGame(TreeMap<Integer, Player> players, Deck deck, Controller... users) {
        Game.reset();
        g = Game.getInstance();
        g.setPlayers(players);
        g.setDeck(deck);

        this.users = users;
        for (Controller c : users) {
            c.setInputListener(this);
            c.start();
        }
    }

    /* ------------------------------ */

    public void play() throws InterruptedException {
        setupFirstTurn();

        boolean isTurnOver;
        while (!g.isOver()) {
            turnStart(player);

            isTurnOver = false;
            while (!isTurnOver) {
                makeChoice();
                parseChoice();
                isTurnOver = resolveChoice();

                if (g.didPlayerWin(player)) {
                    endGame();
                    return;
                }
            }

            turnEnd();
        }
    }

    private void setupFirstTurn() {
        Actions.shuffle();
        Card firstCard = Actions.takeFromDeck();
        Actions.changeCurrentCard(firstCard);
        events.notify("cardPlayed", firstCard);

        for (Player p : g.players())
            Actions.dealFromDeck(p, 7);
        player = g.getPlayer(0);
        events.notify("playerDrew", player);

        for (Controller c : users)
            c.setupControls();

        events.notify("gameStart", g.players());

    }

    private void turnStart(Player p) {
        events.notify("turnStart", p);
        g.setTurn(p);
        player = p;
    }

    private void makeChoice() throws InterruptedException {
        if (player.isHuman())
            synchronized (this) {
                wait();
            }
        // enemy decision
        else
            player.getHand().stream().filter(g::isPlayable).findAny()
                    .ifPresentOrElse(c -> choice = c, () -> choice = "draw");
    }

    private void parseChoice() {
        if (choice instanceof Card)
            choiceType = "card";
        else if (choice instanceof String) {
            switch ((String) choice) {
                case "draw":
                    choiceType = "draw";
                    break;
                case "unoDeclared":
                    choiceType = "unoDeclared";
                    break;
                default:
                    throw new Error(
                            "The Loop said: someone notifyed me \"" + choice + "\" but I can't handle that event!");
            }
        } else if (choice instanceof Integer) {
            choiceType = "cardPosition";
        }
    }

    // Return true if after the method player's turn should end, false otherwise
    private boolean resolveChoice() {
        return choiceTypes.getOrDefault(choiceType, () -> {
            events.notify("warning", "Invalid choice!");
            return false;
        }).get();
    }

    protected void turnEnd() {
        events.notify("turnEnd", player);
        player = g.getPlayer((g.getTurn() + 1) % g.countPlayers());
        choice = null;
        choiceType = null;
        unoDeclared = false;
    }

    private void endGame() {
        events.notify("playerWon", player);
    }

    /* INPUTLISTENER */
    /* ------------- */
    @Override
    public void accept(Object choice, Player source) {
        synchronized (this) {
            // We use == instead of equals because they have to be the same object
            if (source != player) {
                events.notify("warning", "This is not your turn!");
                return;
            }
            this.choice = choice;
            notify();
        }
    }

    /* -------------------------------------- */
    private void startUnoTimer() {
        Player unoer = player;
        events.notify("unoDeclared", unoer);
        unoDeclared = true;

        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                }
                // We use == because they have to be the same. We check if it is still the unoer
                // turn because we don't want to modify the unoDeclared for others player.
                if (unoer == player)
                    unoDeclared = false;
            }
        }.start();
    }
}
