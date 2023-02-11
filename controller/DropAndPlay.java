package controller;

import java.util.HashMap;

import javafx.scene.input.MouseEvent;
import javafx.scene.Node;

/* --- Mine ------------------------------- */

import controller.behaviors.BehaviorDecorator;
import controller.behaviors.DragAndDrop;

/**
 * With the left click on the card, the user will start to drag it. If then
 * that card is realised on the play-zone, it will be also played. The playzone
 * can be set with <code>setPlayzone</code> method.
 */
public class DropAndPlay extends BehaviorDecorator<MouseEvent> {
    /* --- Fields ----------------------------- */

    private Object sourceID;
    private static Node playzone;

    /* ---.--- Getters and Setters ------------ */

    /**
     * Sets the play-zone (or drop target) in which the card dragged should be
     * dropped to be played.
     * 
     * @param playzone The drop target node.
     */
    public static void setPlayzone(Node playzone) {
        DropAndPlay.playzone = playzone;
    }

    /* --- Constructors ----------------------- */

    /**
     * Applies the drag-and-play control to the given card, which has the given tag.
     * 
     * @param source   The node representation of the card.
     * @param sourceID The card identifier.
     */
    public DropAndPlay(Node source, Object sourceID) {
        super(new DragAndDrop(source, playzone));
        wrappee.setStatusCheck(x -> CUController.isActive());
        this.sourceID = sourceID;
    }

    
    /** 
     * @param e
     */
    /* --- BehaviorDecorator ------------------ */

    @Override
    public void onEnd(MouseEvent e) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("choice-type", "FROM_HAND_PLAY_TAG");
        data.put("choice", this.sourceID);
        CUController.communicate("TURN_DECISION", data);
    }
}
