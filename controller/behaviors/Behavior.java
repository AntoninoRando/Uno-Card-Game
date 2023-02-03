package controller.behaviors;

import java.util.function.Consumer;

import controller.CUController;
import javafx.scene.Node;
import javafx.scene.input.InputEvent;

/**
 * A <code>class</code> representation of an input behavior or event, like mouse
 * click, drag and drop, key press, scroll, gesture.
 * <p>
 * This class <em>is not</em> a concrete input event. This class' task is to
 * describe a behavior and detect it, whereas the <code>BehvarioDecorator</code>
 * defines an action to fire after that input event has been detected.
 */
public abstract class Behavior<T extends InputEvent> {
    /* --- Fields ----------------------------- */

    /**
     * The source that will hold this behavior.
     */
    protected Node source;
    /**
     * The action to perform after this behavior has been exhibited.
     */
    protected Consumer<T> onEnd = this::onEnd;

    /* --- Body ------------------------------- */

    /**
     * Sets the action to perform after this behavior has been exhibited.
     * 
     * @param onEnd The action to be performed by the <code>onEnd</code> method.
     */
    public void setEnd(Consumer<T> onEnd) {
        this.onEnd = onEnd;
    }

    /**
     * If this object exhibited the behavior (i.e., <code>behave</code> returns
     * <code>true</code>), performs the action implemented in this method.
     * 
     * @param e The input event used to gather data about the behavior.
     */
    public void onEnd(T e) {
        if (!checkStatus())
            return;

        if (!behave(e))
            return;

        onEnd.accept(e);
    }

    public boolean checkStatus() {
        return CUController.isActive();
    }

    /**
     * The conditions that describe this behavior.
     * 
     * @param e The input event used to gather data about the behavior.
     * @return <code>true</code> if the behavior has been exhibited, and thus the
     *         <code>onEnd</code> action should be fired.
     */
    protected abstract boolean behave(T e);

    /**
     * Adds a series of event listeners to a source, that combined
     * form this desired behavior. Event listeneres already attached to the source
     * <b>should not</b> be deleted.
     */
    protected abstract void applyBehavior();
}
