package controller;

import java.util.HashMap;

import javafx.scene.input.MouseEvent;

import controller.behaviors.BehaviorDecorator;
import controller.behaviors.DragAndDrop;
import javafx.scene.Node;

import events.Event;

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
    }

    /* --- Body ------------------------------- */

    // Con il metodo update di eventListener si può ascoltare all'evento che faccia
    // cambiare la playzone

    @Override
    public void onEnd(MouseEvent e) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("choice-type", "FROM_HAND_PLAY_TAG");
        data.put("choice", this.sourceTag);
        CUController.communicate(Event.TURN_DECISION, data);
    }
}
