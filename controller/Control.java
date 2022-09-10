package controller;

import java.util.function.Consumer;

import events.toController.ViewListener;
import events.toModel.InputListener;

/**
 * A class representing a performable-by-the-user action in the GUI.
 */
public abstract class Control implements ViewListener {
    protected InputListener listener;
    protected static InputListener globalListener;
    protected Consumer<InputListener> action; // TODO fare che questo è un action wrapper per l'action passata come
                                              // argomento (con Decorator Patter)

    /**
     * Sets the InputListener that will react to inputs.
     * 
     * @param listener Class that will react to user inputs.
     */
    public void setListener(InputListener listener) {
        this.listener = listener;
    }

    public static void setGlobalListener(InputListener listener) {
        Control.globalListener = listener;
    }

    /**
     * Sets the action to perform when the user exploit this control.
     * 
     * @param action Action to perform that will send the input to the
     *               <code>InputListener</code>.
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
