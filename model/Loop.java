package model;

import model.events.EventManager;
import model.events.EventListener;

import java.util.TreeMap;

import controller.Controller;
import model.cards.Card;
import model.cards.Deck;

public class Loop {
    /* IMPLEMENTING SINGLETON PATTERN */
    /* ------------------------------ */
    private static Loop instance;

    public static Loop getInstance() {
        if (instance == null)
            instance = new Loop();
        return instance;
    }

    private Loop() {
        events = new EventManager();
    }

    /* ------------------------------ */

    public EventManager events;
    private Game g;
    private Player player;
    private Object choice;
    private String choiceType;

    public void play(EventListener displayer, String[] eventsToListen, TreeMap<Integer, Player> players, Deck deck,
            Controller... users)
            throws InterruptedException {
        Game.reset();
        g = Game.getInstance();
        g.setPlayers(players);
        g.setDeck(deck);

        setupView(displayer, eventsToListen);
        setupGame();
        for (Controller c : users)
            c.start();

        boolean isTurnOver;
        while (!g.isOver) {
            turnStart(player);

            isTurnOver = false;
            while (!isTurnOver) {
                makeChoice();
                parseChoice();
                isTurnOver = resolveChoice();
            }

            turnEnd();
        }
    }

    public void setupView(EventListener displayer, String[] eventsToListen) {
        for (String event : eventsToListen)
            events.subscribe(event, displayer);
    }

    private void setupGame() {
        Actions.shuffle();
        Card firstCard = Actions.takeFromDeck();
        Actions.changeCurrentCard(firstCard);
        events.notify("cardPlayed", firstCard);

        for (int i = 0; i < g.countPlayers(); i++)
            Actions.dealFromDeck(i, 7);
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
            choice = events.waitFor("choiceMade" + player.ID);
        else // enemy decision
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
                Actions.dealFromDeck(1, g.getTurn(player));
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
}
