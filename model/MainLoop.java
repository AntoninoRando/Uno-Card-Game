package model;

import java.util.TreeMap;

import controller.Controller;
import model.cards.Card;
import model.cards.Deck;
import model.listeners.DrawListener;
import model.listeners.EndListener;
import model.listeners.HandListener;
import model.listeners.InputListener;
import model.listeners.InvalidActionListener;
import view.ConsoleOutput;

/**
 * This class represent an instance of an actual running game.
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
    }

    /* INTERFACES METHODS */
    /* ------------------ */
    private HandListener handListener = ConsoleOutput.getInstance();
    private InvalidActionListener invalidActionListener = ConsoleOutput.getInstance();
    private EndListener endListener = ConsoleOutput.getInstance();
    private DrawListener drawListener = ConsoleOutput.getInstance();

    @Override
    public void validate(int choice, Player source) {
        if (!game.getPlayer().equals(source)) {
            invalidActionListener.warn("This is not your turn!");
        } else if (choice == 0) {
            Actions.dealFromDeck(game.getTurn());
            handListener.handChanged(source.hand, source.nickname);
            drawListener.playerDrew("");
            playTurn(game.getTurn());
            enemiesTurn();
        } else if (Actions.isPlayable(game.terrainCard, source.hand.get(choice - 1))) {
            Actions.changeCurrentCard(source.hand.remove(choice - 1));
            handListener.handChanged(source.hand, source.nickname);
            playTurn(game.getTurn());
            enemiesTurn();
        } else {
            Actions.tryChangeCard(source.hand.get(choice - 1));
            invalidActionListener.warn("Can't play it now!");
        }
    }

    /* MAIN LOOP LOGIC */
    /* --------------- */
    private Game game;

    public void setup() {
        Actions.shuffle();
        for (int i = 1; i <= game.countPlayers(); i++)
            Actions.dealFromDeck(i, 5);
        Actions.changeCurrentCard(Actions.takeFromDeck());
        handListener.handChanged(game.getPlayer(1).hand, game.getPlayer(1).nickname);
    }

    // This method is invoked when a valid input from the playing user occurs.
    private void playTurn(int i) {
        if (game.getPlayer(i).hand.isEmpty()) {
            game.end();
            endListener.playerWon(game.getPlayer(i).nickname);
        }
        game.nextTurn();
    }

    private void enemiesTurn() {
        while (!game.isOver() && !game.getPlayer().isHuman) 
            playEnemy();
    }

    private void playEnemy() {
        Player enemy = game.getPlayer();
        for (Card c : enemy.getHand()) {
            if (Actions.tryChangeCard(c)) {
                enemy.hand.remove(c);
                playTurn(game.getTurn());
                return;
            }
        }
        Actions.dealFromDeck(game.getTurn());
        drawListener.playerDrew("");
        playTurn(game.getTurn());
    }

    public void play(TreeMap<Integer, Player> players, Deck deck, Controller... users) {
        game = Game.getInstance();
        game.reset();
        game.setPlayers(players);
        game.setDeck(deck);
        setup();

        for (Controller c : users) {
            c.setInputListener(this);
            c.on();
        }
    }
}
