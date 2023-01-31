package controller;

import java.util.HashMap;

import javafx.scene.Node;

/* --- Mine ------------------------------- */

import events.EventListener;
import events.EventManager;
import events.Event;

import model.CUModel;

/**
 * The mediator between the model and the controller. This is responsible of
 * cuttting of direct communications between controller elements and model
 * elements. It is also responsible of decoding data notified and manipulate
 * those data to bring them in an accetable form for the controller elements.
 */
public class CUController extends EventManager implements EventListener {
    /* --- Singleton -------------------------- */

    private static CUController instance;

    public static CUController getInstance() {
        if (instance == null)
            instance = new CUController();
        return instance;
    }

    private CUController() {
    }

    /* --- Fields ----------------------------- */

    /**
     * The CUModel.
     */
    private static CUModel receiverCU = CUModel.getInstance();

    /* --- Body ------------------------------- */

    /**
     * Notifies the receiver CU (i.e., the CUModel).
     * 
     * @param event The type of event to notify.
     * @param data  The data associatd with the event.
     */
    public static void communicate(Event event, HashMap<String, Object> data) {
        receiverCU.update(event, data);
    }

    /* --- Observer --------------------------- */

    @Override
    public void update(Event event, HashMap<String, Object> data) {
        switch (event) {
            case USER_DREW:
                Node card = (Node) data.get("card-node");
                int cardTag = (int) data.get("card-tag");
                new DropAndPlay(card, cardTag);
                break;
            case USER_SELECTING_CARD:
                Node[] cards = (Node[]) data.get("all-card-nodes");
                int[] cardTags = (int[]) data.get("all-card-tags");
                for (int i = 0; i < cards.length; i++)
                    Controls.applySelectControl(cards[i], cardTags[i]);
                break;
            default:
                notify(event, data);
                break;
        }
    }
}
