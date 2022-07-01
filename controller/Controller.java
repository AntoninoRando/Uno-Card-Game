package controller;

import model.listeners.InputListener;

public abstract class Controller {
    protected int source;
    protected InputListener inputListener;

    /* GETTERS AND SETTERS */
    /* ------------------- */
    public int getSource() {
        return source;
    }

    public void setSource(int s) {
        source = s;
    }

    public void setInputListener(InputListener l) {
        inputListener = l;
    }

    /* CORE METHODS */
    /* ------------ */
    // Since the method is invoked, the controller is always ready to take input.
    public abstract void on();
}