package controller;

import java.util.function.Consumer;

import events.InputListener;
import events.ViewListener;

/**
 * A class representing a performable-by-the-user action in the GUI.
 */
public abstract class Control implements ViewListener {
    protected InputListener listener;
    protected Consumer<InputListener> action;
    
    public void setListener(InputListener listener) {
        this.listener = listener;
    }

    public void setAction(Consumer<InputListener> action) {
        this.action = action;
    }

    public void fire() {
        action.accept(listener);
    }
}
