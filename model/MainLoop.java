package model;

import java.util.Map.Entry;

import model.cards.Card;
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

    @Override
    public void validate(int choice, int source) {
        if (source != game.turn) {
            invalidActionListener.warn("This is not your turn!");
            return;
        }

        Player p = game.players.get(source);

        if (choice == 0) {
            Actions.dealFromDeck(game, source);
            handListener.handChanged(p.hand, p.nickname);
            playTurn(source);
            enemiesTurn();
        } else if (Actions.isPlayable(game.terrainCard, p.hand.get(choice - 1))) {
            Actions.changeCurrentCard(game, p.hand.remove(choice - 1));
            handListener.handChanged(p.hand, p.nickname);
            playTurn(source);
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
            Actions.dealFromDeck(game, i, 3);
        Actions.changeCurrentCard(game, Actions.takeFromDeck(game));
        handListener.handChanged(game.players.get(1).hand, game.players.get(1).nickname);
    }

    // This method is invoked when a valid input from the playing user occurs.
    private void playTurn(int i) {
        if (game.players.get(i).hand.isEmpty()) {
            game.end = true;
            endListener.playerWon(game.players.get(i).nickname);
            for (Player p : game.players.values())
                p.controller.ifPresent(c -> c.off());
        } else if (++game.turn > game.players.size())
            game.turn = 1;
    }

    private void enemiesTurn() {
        while (!game.end && game.players.get(game.turn).controller.isEmpty()) {
            playEnemy();
        }
    }

    private void playEnemy() {
        Player enemy = game.players.get(game.turn);
        for (Card c : enemy.getHand()) {
            if (Actions.tryChangeCard(game, c)) {
                enemy.hand.remove(c);
                playTurn(game.turn);
                break;
            }
        }
    }

    public void play(Game game) {
        this.game = game;
        game.reset();
        setup();
        for (Entry<Integer, Player> e : game.players.entrySet())
            e.getValue().controller.ifPresent(c -> {
                c.setSource(e.getKey());
                c.setInputListener(this);
                c.on();
            });
    }
}
