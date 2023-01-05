package controller;

import java.util.HashMap;

import javafx.scene.input.MouseEvent;

import controller.behaviors.BehaviorDecorator;
import controller.behaviors.DragAndDrop;
import javafx.scene.Node;

import events.EventType;

public class DropAndPlay extends BehaviorDecorator<MouseEvent> {
    /* --- Fields ----------------------------- */

    // wrappee
    private int sourceTag;
    private static Node playzone;

    /* ---.--- Getters and Setters ------------ */

    public static void setPlayzone(Node playzone) {
        DropAndPlay.playzone = playzone;
    }

    /* --- Constructors ----------------------- */

    public DropAndPlay(Node source, int sourceTag) {
        super(new DragAndDrop(source, playzone));
        this.sourceTag = sourceTag;
        wrappee.setEnd(this::onEnd);
    }

    /* --- Body ------------------------------- */

    // Con il metodo update di eventListener si può ascoltare all'evento che faccia
    // cambiare la playzone

    @Override
    public void onEnd(MouseEvent e) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("choice-type", "card-play-by-tag");
        data.put("choice", this.sourceTag);
        CUController.getInstance().communicate(EventType.TURN_DECISION, data);
    }
}
