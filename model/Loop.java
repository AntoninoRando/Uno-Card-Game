package model;

import model.events.EventManager;
import model.events.InputListener;
import view.Displayer;

import java.util.HashMap;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Supplier;

import controller.Controller;
import model.cards.Card;
import model.cards.Deck;

public class Loop implements InputListener {
    /* IMPLEMENTING SINGLETON PATTERN */
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
                unoDeclared.ifPresent(x -> {
                    if (x == 1) {
                        Actions.dealFromDeck(player, 4);
                        events.notify("playerDrew", player);
                    }
                });

                player.hand.remove(c);
                c.getEffect().ifPresent(e -> e.cast(player, c));
                events.notify("cardPlayed", c);

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
        choiceTypes.put("null", () -> false);
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

    // 1 = must be declared, 0 = declared, empty = don't needed
    private Optional<Integer> unoDeclared = Optional.empty();

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

        unoDeclared = player.getHand().size() == 2 ? Optional.of(1) : Optional.empty();
    }

    private void makeChoice() throws InterruptedException {
        events.notify("playerChoosing", player);
        if (player.isHuman)
            synchronized (this) {
                wait();
            }
        // enemy decision
        else
            player.getHand().stream().filter(g::isPlayable).findAny()
                    .ifPresentOrElse(c -> choice = c, () -> choice = "draw");
    }

    private void parseChoice() {
        events.notify("processingChoice", choice);
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
        }
    }

    // Return true if after the method player's turn should end, false otherwise
    private boolean resolveChoice() {
        events.notify("resolvingChoice", choice);
        return choiceTypes.getOrDefault(choiceType, () -> {
            events.notify("warning", "Invalid choice!");
            return false;
        }).get();
    }

    private void turnEnd() {
        events.notify("turnEnd", player);
        player = g.getPlayer((g.getTurn() + 1) % g.countPlayers());
        choice = null;
        choiceType = null;
    }

    private void endGame() {
        events.notify("playerWon", player);
    }

    /* INPUTLISTENER */
    /* ------------- */
    // TODO aggiustare come funzionano i thread: ogni controller umano ha un suo
    // thread. I metodi seguenti devono funzionare nel thread del controller, per
    // non fermare l'esecuzione del loop. Per adesso sembri funzioni, ma non sono
    // sicuro.
    @Override
    public void accept(int choice, Player source) {
        synchronized (this) {
            // We use == instead of equals because they have to be the same object
            if (source != player) {
                events.notify("warning", "This is not your turn!");
                return;
            }
            this.choice = source.getHand().get(choice);
            notify();
        }
    }

    @Override
    public void accept(String choice, Player source) {
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
        // TODO
    }

    /* -------------------------------------- */
    // TODO creare un Action o Effect che si chiama "startTimerWithFinalEffect" e
    // fare che questo metodo lo usa
    private void startUnoTimer() {
        unoDeclared.ifPresent(state -> {
            if (state != 1)
                return;

            unoDeclared = Optional.of(0);
            new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                    }
    
                    unoDeclared = Optional.of(1); // TODO c'è un bug
                }
            }.start();
        });
    }
}
