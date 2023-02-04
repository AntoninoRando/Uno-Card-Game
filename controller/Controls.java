package controller;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.Supplier;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/* --- JUno ------------------------------- */

import controller.behaviors.Behavior;
import controller.behaviors.BehaviorDecorator;
import controller.behaviors.Click;
import controller.behaviors.KeyPress;

import events.Event;

/**
 * Some important <code>BehaviorDecorator</code> instances.
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
    public static final void applySelectControl(Node source, int cardTag) {
        Behavior<MouseEvent> wrappee = new Click(source, new boolean[] { false }, null);
        new BehaviorDecorator<>(wrappee) {
            @Override
            public void onEnd(MouseEvent e) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("choice-type", "SELECTION_COMPLETED");
                data.put("choice", cardTag);
                CUController.communicate(Event.SELECTION, data);
            }
        };
    }

    /**
     * With the left click on the node, the user will change an info on its profile.
     * 
     * @param source The node to click.
     * @param field  The type of the profile info to change, associated with the new
     *               value.
     */
    public static final void applyInfoChange(Node source, Entry<String, String> field) {
        Behavior<MouseEvent> wrappee = new Click(source, new boolean[] { false }, null);
        new BehaviorDecorator<>(wrappee) {
            @Override
            public void onEnd(MouseEvent e) {
                HashMap<String, Object> data = new HashMap<>();
                data.put(field.getKey(), field.getValue());
                CUController.communicate(Event.INFO_CHANGE, data);
            }
        };
    }

    /**
     * With the left click on the node, the user will reset all the info on its
     * profile.
     * 
     * @param source The node to click.
     */
    public static final void applyInfoReset(Node source) {
        Behavior<MouseEvent> wrappee = new Click(source, new boolean[] { false }, null);
        new BehaviorDecorator<>(wrappee) {
            @Override
            public void onEnd(MouseEvent e) {
                CUController.communicate(Event.INFO_RESET, null);
            }
        };
    }

    /**
     * When pressing the enter key upon writing on the source node, the user will
     * change its nickname.
     * 
     * @param source The node in which the user write, specifically the enter key.
     * @param nick   A supplier for the new nickname.
     */
    public static final void applyNickEnter(Node source, Supplier<String> nick) {
        Behavior<KeyEvent> wrappee = new KeyPress(source, KeyCode.ENTER.getCode());
        new BehaviorDecorator<>(wrappee) {
            @Override
            public void onEnd(KeyEvent e) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("nickname", nick.get());
                CUController.communicate(Event.INFO_CHANGE, data);
            }
        };
    }
}