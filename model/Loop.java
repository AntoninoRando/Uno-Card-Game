package model;

import model.events.EventManager;
import model.events.InputListener;
import view.Displayer;

import java.util.HashMap;
import java.util.LinkedList;
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
                g.getPlayer().hand.remove(c);
                c.getEffect().ifPresent(effect -> effect.cast(g.getPlayer(), c));
                events.notify("cardPlayed", c);

                if (g.getPlayer().getHand().size() == 1 && !unoDeclared) {
                    Actions.dealFromDeck(g.getPlayer(), 2);
                    events.notify("playerDrew", g.getPlayer());
                }

                events.notify("playerHandChanged", g.getPlayer());
                return true;
            } else {
                events.notify("warning", "Can't play it now!");
                return false;
            }
        });
        choiceTypes.put("draw", () -> {
            Actions.dealFromDeck(g.getPlayer());
            events.notify("playerDrew", g.getPlayer());
            return true;
        });
        choiceTypes.put("unoDeclared", () -> {
            startUnoTimer();
            return false;
        });
        choiceTypes.put("cardPosition", () -> {
            try {
                choice = g.getPlayer().getHand().get((int) choice);
                return choiceTypes.get("card").get();
            } catch (IndexOutOfBoundsException e) {
                events.notify("warning", "Invalid selection!");
                return false;
            }
        });
    }

    /* ------------------------------ */

    public EventManager events;
    HashMap<String, Supplier<Boolean>> choiceTypes;
    private Displayer disp;

    private Game g;
    private Player player;
    Object choice;
    String choiceType;

    private Controller[] users;

    boolean unoDeclared;

    LinkedList<Phase> phases = new LinkedList<>();
    int currentPhase;

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
        phases.add(Phases.START_TURN);
        phases.add(Phases.MAKE_CHOICE);
        phases.add(Phases.PARSE_CHOICE);
        phases.add(Phases.RESOLVE_CHOICE);
        phases.add(Phases.END_TURN);
    }

    /* ------------------------------ */

    public void play() throws InterruptedException {
        setupFirstTurn();

        while (!g.isOver()) {
            boolean validChoice = phases.get(currentPhase).apply(this, Game.getInstance());

            if (g.didPlayerWin(g.getPlayer())) {
                endGame();
                return;
            }

            if (phases.get(currentPhase) == Phases.RESOLVE_CHOICE && !validChoice)
                currentPhase = 1;
            else
                currentPhase = (++currentPhase) % phases.size();
        }
    }

    private void setupFirstTurn() {
        events.notify("gameStart", g.getPlayers());

        Actions.shuffle();
        Card firstCard = Actions.takeFromDeck();
        Actions.changeCurrentCard(firstCard);
        events.notify("firstCard", firstCard);

        for (Player p : g.getPlayers()) {
            Actions.dealFromDeck(p, 7);
            events.notify("playerDrew", p);
        }

        player = g.getPlayer(0);

        for (Controller c : users)
            c.setupControls();

    }

    private void endGame() {
        events.notify("playerWon", player);
    }

    public static void reset() {
        instance.events.notify("reset", null);
        Game.reset();
        instance = null;
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