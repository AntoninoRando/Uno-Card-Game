package controller;

import model.Player;
import model.listeners.InputListener;

/**
 * This class enables a player to take choices (input) that modify the game
 * state.
 */
public abstract class Controller {
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

    /* CORE METHODS */
    /* ------------ */
    // Since the method is invoked, the controller is always ready to take inputs.
    public abstract void on();

    // Since the method is invoked, the controller will not take inputs.
    public abstract void off();
}