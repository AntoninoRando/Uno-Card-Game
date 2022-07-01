package model;

import java.util.Map.Entry;

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
    
    @Override
    public void validate(int choice, int source) {
        if (source != game.turn) {
            invalidActionListener.warn("This is not your turn!");
            return;
        }
        
        Player p = game.players.get(source);
        if (Actions.isPlayable(game.terrainCard, p.hand.get(choice))) {
            Actions.changeCurrentCard(game, p.hand.remove(choice));
            handListener.handChanged(p.hand, p.nickname);
            playTurn(source);
        }
        else {
            Actions.tryChangeCard(game, p.hand.get(choice));
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
        handListener.handChanged(game.players.get(1).hand, game.players.get(1).nickname);
        Actions.changeCurrentCard(game, Actions.takeFromDeck(game));
    }

    // This method is invoked when a valid input from the playing user occurs.
    private void playTurn(int i) {
        game.turn = ++game.turn >= game.players.size() ? 1 : ++game.turn;
        // TO-DO! It should check for user win, then for game end.

    }

    public void play(Game game) {
        this.game = game;
        game.reset();
        setup();
        for (Entry<Integer, Player> e : game.players.entrySet()) {
            e.getValue().controller.setSource(e.getKey());
            e.getValue().controller.setInputListener(this);
            e.getValue().controller.on();
        }
    }
}
