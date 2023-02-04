package controller;

import java.util.HashMap;
import java.util.function.Supplier;

import javafx.scene.Node;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;

/* --- JUno ------------------------------- */

import controller.behaviors.Behavior;
import controller.behaviors.BehaviorDecorator;
import controller.behaviors.Click;

import events.Event;

/**
 * Some important <code>BehaviorDecorator</code> objects.
 */
public abstract class Controls {
    /**
     * With a left click on the source, the user will draw a card.
     * 
     * @param source The node to be clicked to draw one.
     */
    public static final void applyDrawControl(Node source) {
        Behavior<MouseEvent> wrappee = new Click(source, new boolean[] { false }, null);
        new BehaviorDecorator<>(wrappee) {
            @Override
            public void onEnd(MouseEvent e) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("choice-type", "FROM_DECK_DRAW");
                data.put("choice", 1);
                CUController.communicate(Event.TURN_DECISION, data);
            }
        };
    }

    /**
     * With the right click on the source, the user will declare UNO.
     * 
     * @param source The node to be clicked to declare UNO.
     */
    public static final void applyUnoControl(Node source) {
        Behavior<MouseEvent> wrappee = new Click(source, new boolean[] { true }, null);
        new BehaviorDecorator<>(wrappee) {
            @Override
            public void onEnd(MouseEvent e) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("choice-type", "SAY_UNO");
                data.put("choice", 0);
                CUController.communicate(Event.TURN_DECISION, data);
            }
        };
    }

    /**
     * With the left click on the card, the user will select that card.
     * 
     * @param source  Card to click.
     * @param cardTag The tag of the card.
     */
    public static final void applySelectControl(Node source, Object cardID) {
        Behavior<MouseEvent> wrappee = new Click(source, new boolean[] { false }, null);
        new BehaviorDecorator<>(wrappee) {
            @Override
            public void onEnd(MouseEvent e) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("choice-type", "SELECTION_COMPLETED");
                data.put("choice", cardID);
                CUController.communicate(Event.SELECTION, data);
            }
        };
    }

    /**
     * With the left click on the node, the user will change an info on its profile.
     * 
     * @param behavior The behavior (or input, gesture, action) the user must
     *                 perform to execute this.
     * @param field    A getter for the field to change (e.g., nickname, icon)
     * @param value    The value of the field supplied.
     */
    public static final <T extends InputEvent> void applyInfoChange(Behavior<T> behavior,
            Supplier<String> field, Supplier<Object> value) {
        new BehaviorDecorator<>(behavior) {
            @Override
            public void onEnd(T e) {
                HashMap<String, Object> data = new HashMap<>();
                data.put(field.get(), value.get());
                CUController.communicate(Event.INFO_CHANGE, data);
            }
        };
    }
}