package controller;

import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

import controller.behaviors.BehaviorDecorator;
import controller.behaviors.Click;
import events.EventType;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public abstract class Controls {
    public static final Function<Node, BehaviorDecorator<MouseEvent>> draw = source -> new BehaviorDecorator<MouseEvent>(
            new Click(source, new boolean[] { false }, null)) {

        @Override
        public void onEnd(MouseEvent e) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("choice-type", "deck-draw");
            data.put("choice", 1);
            CUController.getInstance().communicate(EventType.TURN_DECISION, data);
        }
    };

    public static final Function<Node, BehaviorDecorator<MouseEvent>> uno = source -> new BehaviorDecorator<MouseEvent>(
            new Click(source, new boolean[] { true }, null)) {

        @Override
        public void onEnd(MouseEvent e) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("choice-type", "say-uno");
            data.put("choice", null);
            CUController.getInstance().communicate(EventType.TURN_DECISION, data);
        }
    };

    public static final BiFunction<Node, Integer, BehaviorDecorator<MouseEvent>> select = (source,
            cardTag) -> new BehaviorDecorator<MouseEvent>(new Click(source, new boolean[] { false }, null)) {

                @Override
                public void onEnd(MouseEvent e) {
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("choice-type", "card-selected");
                    data.put("choice", cardTag);
                    CUController.getInstance().communicate(EventType.SELECTION, data);
                }
            };
}