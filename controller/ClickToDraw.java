package controller;

import java.util.HashMap;

import controller.behaviors.BehaviorDecorator;
import controller.behaviors.Click;
import events.EventType;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * Through this control the user can draw a card.
 */
public class ClickToDraw extends BehaviorDecorator<MouseEvent> {
    /* --- Constructors ----------------------- */

    public ClickToDraw(Node source) {
        super(new Click(source, new boolean[] {false}, null));
        wrappee.setEnd(this::onEnd);
    }

    /* --- Body ------------------------------- */

    @Override
    public void onEnd(MouseEvent e) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("choice-type", "deck-draw");
        data.put("choice", 1);
        CUController.getInstance().communicate(EventType.TURN_DECISION, data);
    }
}
