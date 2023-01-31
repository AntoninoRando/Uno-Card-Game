package controller.behaviors;

import java.util.function.Consumer;

import javafx.scene.input.InputEvent;

/**
 * An action to perform after the behavior, described by the
 * <code>Behavior</code> class, has been exhibited (i.e.,
 * <code>Behavior.behave</code> returns <code>true</code>).
 */
public class BehaviorDecorator<T extends InputEvent> {
    /* --- Fields ----------------------------- */

    /**
     * The behavior, or event, that must be exhibited to execute this action.
     */
    protected Behavior<T> wrappee;

    /* --- Constructors ----------------------- */

    /**
     * Constructs a concrete action to execute afther the input behavior exhibites.
     * 
     * @param wrappee The behavior that must be performed to execute this action.
     */
    public BehaviorDecorator(Behavior<T> wrappee) {
        this.wrappee = wrappee;
        wrappee.setEnd(this::onEnd);
    }

    /* --- Body ------------------------------- */

    /**
     * Calls <code>setEnd(onEnd)</code> on the <code>wrappee</code>
     */
    public void setEnd(Consumer<T> onEnd) {
        wrappee.setEnd(onEnd);
    }

    /**
     * Calls <code>onEnd(e)</code> on the <code>wrappee</code>
     */
    public void onEnd(T e) {
        wrappee.onEnd(e);
    }
}
