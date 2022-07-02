package model;

import controller.Controller;
import model.cards.Card;
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
        if (!game.players.get(game.turn).equals(source)) {
            invalidActionListener.warn("This is not your turn!");
            return;
        }

        Player p = game.players.get(game.turn);

        if (choice == 0) {
            Actions.dealFromDeck(game, game.turn);
            handListener.handChanged(p.hand, p.nickname);
            drawListener.playerDrew("");
            playTurn(game.turn);
            enemiesTurn();
        } else if (Actions.isPlayable(game.terrainCard, p.hand.get(choice - 1))) {
            Actions.changeCurrentCard(game, p.hand.remove(choice - 1));
            handListener.handChanged(p.hand, p.nickname);
            playTurn(game.turn);
            enemiesTurn();
        } else {
            Actions.tryChangeCard(game, p.hand.get(choice - 1));
            invalidActionListener.warn("Can't play it now!");
        }
    }

    /* MAIN LOOP LOGIC */
    /* --------------- */
    private Game game;

    public void setup() {
        Actions.shuffle(game);
        for (int i : game.players.keySet())
            Actions.dealFromDeck(game, i, 5);
        Actions.changeCurrentCard(game, Actions.takeFromDeck(game));
        handListener.handChanged(game.players.get(1).hand, game.players.get(1).nickname);
    }

    // This method is invoked when a valid input from the playing user occurs.
    private void playTurn(int i) {
        if (game.players.get(i).hand.isEmpty()) {
            game.end = true;
            endListener.playerWon(game.players.get(i).nickname);
        } else if (++game.turn > game.players.size())
            game.turn = 1;
    }

    private void enemiesTurn() {
        while (!game.end && !game.players.get(game.turn).isHuman) {
            playEnemy();
        }
    }

    private void playEnemy() {
        Player enemy = game.players.get(game.turn);
        for (Card c : enemy.getHand()) {
            if (Actions.tryChangeCard(game, c)) {
                enemy.hand.remove(c);
                playTurn(game.turn);
                return;
            }
        }
        Actions.dealFromDeck(game, game.turn);
        drawListener.playerDrew("");
        playTurn(game.turn);
    }

    public void play(Game game, Controller... users) {
        this.game = game;
        game.reset();
        setup();

        for (Controller c : users) {
            c.setInputListener(this);
            c.on();
        }
    }
}
