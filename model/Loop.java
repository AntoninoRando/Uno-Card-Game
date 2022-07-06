package model;

import model.events.EventManager;
import model.events.InputListener;
import model.events.EventListener;

import java.util.TreeMap;

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
    }

    /* ------------------------------ */

    public EventManager events;
    private Game g;
    private Player player;
    private Object choice;
    private String choiceType;

    public void setupView(EventListener displayer, String[] eventsToListen) {
        events = new EventManager();

        for (String event : eventsToListen)
            events.subscribe(event, displayer);
    }

    public void setupGame(TreeMap<Integer, Player> players, Deck deck, Controller... users) {
        Game.reset();
        g = Game.getInstance();
        g.setPlayers(players);
        g.setDeck(deck);

        for (Controller c : users) {
            c.setInputListener(this);
            c.start();
        }
    }

    public void play() throws InterruptedException {
        setupFirstTurn();

        boolean isTurnOver;
        while (!g.isOver) {
            turnStart(player);

            isTurnOver = false;
            while (!isTurnOver) {
                makeChoice();
                parseChoice();
                isTurnOver = resolveChoice();

                if (g.winCondition.test(player)) {
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

        for (Player p : g.players.values())
            Actions.dealFromDeck(p, 7);
        player = g.getPlayer(0);
        events.notify("playerDrew", player);
    }

    private void turnStart(Player p) {
        g.turn = g.getTurn(p);
        player = p;
        events.notify("turnStart", p);
    }

    private void makeChoice() throws InterruptedException {
        if (player.isHuman)
            synchronized (this) {
                wait();
            }
        // enemy decision
        else
            player.getHand().stream().filter(g::isPlayable).findAny()
                    .ifPresentOrElse((c) -> choice = c, () -> choice = "draw");
    }

    private void parseChoice() {
        if (choice instanceof Card)
            choiceType = "card";
        else if (choice instanceof String) {
            switch ((String) choice) {
                case "draw":
                    choiceType = "draw";
                    break;
                default:
                    choiceType = "draw";
                    break;
            }
        }
    }

    // Return true if after the method player's turn should end, false otherwise
    private boolean resolveChoice() {
        switch (choiceType) {
            case "card":
                Card c = (Card) choice;
                if (Actions.tryChangeCard(c)) {
                    player.hand.remove(c);
                    c.getEffect().ifPresent(e -> e.dispatch(c, g.getTurn(player)));
                    events.notify("cardPlayed", c);
                    return true;
                } else {
                    events.notify("warning", "Can't play it now!");
                    return false;
                }
            case "draw":
                Actions.dealFromDeck(player);
                events.notify("playerDrew", player);
                return true;
            default:
                events.notify("warning", "Invalid choice!");
                return false;
        }
    }

    private void turnEnd() {
        events.notify("turnEnd", player);
        player = g.getPlayer((g.turn + 1) % g.countPlayers());
        choice = null;
        choiceType = null;
    }

    private void endGame() {
        events.notify("playerWon", player);
    }

    /* INPUTLISTENER METHODS */
    /* --------------------- */
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
}
