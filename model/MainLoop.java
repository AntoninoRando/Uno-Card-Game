package model;

import java.util.TreeMap;

import controller.Controller;
import model.cards.Card;
import model.cards.Deck;
import model.events.EventManager;
import model.events.InputListener;
import view.ConsoleOutput;

/**
 * This class set in motion a Game object.
 */
public class MainLoop implements InputListener {
    /* IMPLEMENTING SINGLETON PATTERN */
    /* ------------------------------ */
    private static MainLoop instance;

    public static MainLoop getInstance() {
        if (instance == null)
            instance = new MainLoop();
        return instance;
    }

    private MainLoop() {
        events = new EventManager();
    }

    /* EVENTS */
    /* ------ */
    public EventManager events;

    /* INTERFACES METHODS */
    /* ------------------ */
    @Override
    public void validate(int choice, Player source) {
        if (!game.getPlayer().equals(source)) {
            events.notify("Warn", "This is not your turn!");
        } else if (choice == 0) {
            Actions.dealFromDeck(game.getTurn());
            events.notify("PlayerDrew", source);
            events.notify("HandChanged", source);
            playTurn(game.getTurn());
            enemiesTurn();
        } else if (game.isPlayable(source.hand.get(choice - 1))) {
            Card toPlay = source.hand.remove(choice - 1);
            Actions.changeCurrentCard(toPlay);
            events.notify("HandChanged", source);
            events.notify("CardChanged", toPlay);

            toPlay.getEffect().ifPresent(e -> e.dispatch(toPlay, game.getTurn(source)));
            playTurn(game.getTurn());
            enemiesTurn();
        } else {
            Actions.tryChangeCard(source.hand.get(choice - 1));
            events.notify("Warn", "Can't play it now!");
        }
    }

    @Override
    public void validate(String choice, Player source) {
        events.notify("InputGiven", choice);
    }

    /* MAIN LOOP LOGIC */
    /* --------------- */
    private Game game;

    public void setup() {
        Actions.shuffle();

        for (int i = 0; i < game.countPlayers(); i++)
            Actions.dealFromDeck(i, 7);
        
        Card firstCard = Actions.takeFromDeck();
        Actions.changeCurrentCard(firstCard);
        events.notify("CardChanged", firstCard);
        events.notify("HandChanged", game.getPlayer());
    }

    // This method is invoked when a valid input from the playing user occurs.
    public void playTurn(int i) {
        Player p = game.getPlayer(i);

        if (p == null) {
            game.nextTurn();
            return;
        }

        if (p.hand.isEmpty()) {
            game.end();
            events.notify("PlayerWon", p);
        } else {
            game.nextTurn();
            events.notify("PlayerTurn", game.getTurn());
        }
    }

    public void enemiesTurn() {
        while (!game.isOver() && !game.getPlayer().isHuman)
            playEnemy();
    }

    private void playEnemy() {
        Player enemy = game.getPlayer();
        for (Card c : enemy.getHand()) {
            if (Actions.tryChangeCard(c)) {
                enemy.hand.remove(c);
                events.notify("CardChanged", c);
                playTurn(game.getTurn());
                return;
            }
        }
        Actions.dealFromDeck(game.getTurn());
        events.notify("PlayerDrew", enemy);
        playTurn(game.getTurn());
    }

    public void play(TreeMap<Integer, Player> players, Deck deck, Controller... users) {
        ConsoleOutput displayer = ConsoleOutput.getInstance();
        for (String eventType : displayer.getEventsListening())
            events.subscribe(eventType, displayer);

        Game.reset();
        game = Game.getInstance();
        game.setPlayers(players);
        game.setDeck(deck);
        setup();

        for (Controller c : users) {
            c.setInputListener(this);
            c.on();
        }
    }
}
