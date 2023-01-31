package controller;

import java.util.HashMap;

import javafx.scene.input.MouseEvent;
import javafx.scene.Node;

/* --- Mine ------------------------------- */

import controller.behaviors.BehaviorDecorator;
import controller.behaviors.DragAndDrop;

import events.Event;

/**
 * With the left click on the card, the user will start to drag it. If then
 * that card is realised on the play-zone, it will be also played. The playzone
 * can be set with <code>setPlayzone</code> method.
 */
public class DropAndPlay extends BehaviorDecorator<MouseEvent> {
    /* --- Fields ----------------------------- */

    private int sourceTag;
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
     * @param source    The node representation of the card.
     * @param sourceTag The card tag.
     */
    public DropAndPlay(Node source, int sourceTag) {
        super(new DragAndDrop(source, playzone));
        this.sourceTag = sourceTag;
    }

    /* --- BehaviorDecorator ------------------ */

    @Override
    public void onEnd(MouseEvent e) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("choice-type", "FROM_HAND_PLAY_TAG");
        data.put("choice", this.sourceTag);
        CUController.communicate(Event.TURN_DECISION, data);
    }
}
