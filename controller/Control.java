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
    
    /**
     * Sets the InputListener that will react to inputs.
     * @param listener Class that will react to user inputs.
     */
    public void setListener(InputListener listener) {
        this.listener = listener;
    }

    /**
     * Sets the action to perform when the user exploit this control.
     * @param action Action to perform that will send the input to the <code>InputListener</code>.
     */
    public void setAction(Consumer<InputListener> action) {
        this.action = action;
    }

    /**
     * Applies the action related to this control.
     */
    public void fire() {
        action.accept(listener);
    }
}
