package controller;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import controller.behaviors.BehaviorDecorator;
import controller.behaviors.Click;
import controller.behaviors.KeyPress;
import events.Event;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public abstract class Controls {
    public static final Function<Node, BehaviorDecorator<MouseEvent>> draw = source -> new BehaviorDecorator<MouseEvent>(
            new Click(source, new boolean[] { false }, null)) {

        @Override
        public void onEnd(MouseEvent e) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("choice-type", "FROM_DECK_DRAW");
            data.put("choice", 1);
            CUController.communicate(Event.TURN_DECISION, data);
        }
    };

    public static final Function<Node, BehaviorDecorator<MouseEvent>> UNO = source -> new BehaviorDecorator<MouseEvent>(
            new Click(source, new boolean[] { true }, null)) {

        @Override
        public void onEnd(MouseEvent e) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("choice-type", "SAY_UNO");
            data.put("choice", 0);
            CUController.communicate(Event.TURN_DECISION, data);
        }
    };

    public static final BiFunction<Node, Integer, BehaviorDecorator<MouseEvent>> SELECT = (source,
            cardTag) -> new BehaviorDecorator<MouseEvent>(new Click(source, new boolean[] { false }, null)) {

                @Override
                public void onEnd(MouseEvent e) {
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("choice-type", "SELECTION_COMPLETED");
                    data.put("choice", cardTag);
                    CUController.communicate(Event.SELECTION, data);
                }
            };

    public static final BiFunction<Node, Entry<String, String>, BehaviorDecorator<MouseEvent>> INFO_CHANGE = (source,
            field) -> new BehaviorDecorator<MouseEvent>(new Click(source, new boolean[] { false }, null)) {
                @Override
                public void onEnd(MouseEvent e) {
                    HashMap<String, Object> data = new HashMap<>();
                    data.put(field.getKey(), field.getValue());
                    CUController.communicate(Event.INFO_CHANGE, data);
                }
            };
    public static final Function<Node, BehaviorDecorator<MouseEvent>> INFO_RESET = source -> new BehaviorDecorator<MouseEvent>(
            new Click(source, new boolean[] { false }, null)) {
        @Override
        public void onEnd(MouseEvent e) {
            CUController.communicate(Event.INFO_RESET, null);
        }
    };

    public static final BiFunction<Node, Supplier<String>, BehaviorDecorator<KeyEvent>> NICK_ENTER = (
            source, text) -> new BehaviorDecorator<KeyEvent>(new KeyPress(source, KeyCode.ENTER.getCode())) {
                @Override
                public void onEnd(KeyEvent e) {
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("nickname", text.get());
                    CUController.communicate(Event.INFO_CHANGE, data);
                }
            };

    public static final Function<Node, BehaviorDecorator<MouseEvent>> SKIP = source -> new BehaviorDecorator<MouseEvent>(
            new Click(source, new boolean[] { false }, null)) {

        @Override
        public void onEnd(MouseEvent e) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("choice-type", "SKIP");
            data.put("choice", 0);
            CUController.communicate(Event.TURN_DECISION, data);
        }
    };
}