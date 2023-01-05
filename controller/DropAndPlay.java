package controller;

import java.util.HashMap;

import controller.rawObjects.DragAndDrop;
import javafx.scene.Node;

import events.EventType;

public class DropAndPlay extends DragAndDrop {
    /* --- Fields ----------------------------- */

    private int sourceTag;
    private static Node playzone;

    /* ---.--- Getters and Setters ------------ */

    public static void setPlayzone(Node playzone) {
        DropAndPlay.playzone = playzone;
    }

    /* --- Constructors ----------------------- */

    public DropAndPlay(Node source, int sourceTag) {
        super(source, playzone);
        this.sourceTag = sourceTag;

        source.setOnMouseReleased(e -> {
            dragEnd(e);
            if (getDragState())
                sendCommunication();
        });
    }

    /* --- Body ------------------------------- */

    private void sendCommunication() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("choice-type", "card-play");
        data.put("card-tag", this.sourceTag);
        CUController.getInstance().communicate(EventType.TURN_DECISION, data);
    }

    // Con il metodo update di eventListener si può ascoltare all'evento che faccia
    // cambiare la playzone
}
