package controller;

import model.Player;
import model.events.InputListener;

/**
 * This class enables a player to take choices (input) that modify the game
 * state.
 */
public abstract class Controller extends Thread {
    protected Player source;
    protected InputListener inputListener;

    /* GETTERS AND SETTERS */
    /* ------------------- */
    public Player getSource() {
        return source;
    }

    public void setSource(Player s) {
        source = s;
    }

    public void setInputListener(InputListener l) {
        inputListener = l;
    }
}