package controller;

import java.util.HashMap;

import events.EventListener;
import events.EventManager;
import events.EventType;
import javafx.scene.Node;
import model.CUModel;

/**
 * TODO forse è meglio implementarlo seguendo il bridge pattern
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
        subscribeAll();
    }

    /* --- Field ------------------------------ */

    private static CUModel receiverCU = CUModel.getInstance();

    /* --- Body ------------------------------- */

    private void subscribeAll() {
    }

    public void communicate(EventType event, HashMap<String, Object> data) {
        receiverCU.update(event, data);
    }

    /* --- Observer --------------------------- */

    @Override
    public void update(EventType event, HashMap<String, Object> data) {
        switch (event) {
            case USER_DREW:
                Node card = (Node) data.get("card-node");
                int cardTag = (int) data.get("card-tag");
                new DropAndPlay(card, cardTag);
                break;
            default:
                break;
        }
    }
}
