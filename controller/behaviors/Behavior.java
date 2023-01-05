package controller.behaviors;

import java.util.function.Consumer;

import javafx.scene.input.InputEvent;

/**
 * Questa classe implementa il decorator pattern, rivisitato siccome le azioni
 * da eseguire in JAVAFX devono essere specificate da un setOnAction(...).
 * <p>
 * In pratica questa classe è la descrizione di un comportamento, NON l'azione.
 * Ad esempio, può descrivere cosa rende l'azione di un click tale.
 */
public abstract class Behavior<T extends InputEvent> {
    protected Consumer<T> onEnd = this::onEnd;

    public void setEnd(Consumer<T> onEnd) {
        this.onEnd = onEnd;
    }

    /**
     * The action to perform if the behavior have been performed correctly.
     * 
     * @param e
     */
    public void onEnd(T e) {
        if (!behave(e))
            return;

        onEnd.accept(e);
    }

    /**
     * Here the concrete behavior classes implement their behavior.
     * 
     * @param e The event used to gather data about the behavior.
     * @return True if the behavior completed as desired, and then the onEnd action
     *         should be fired.
     */
    public abstract boolean behave(T e);
}
