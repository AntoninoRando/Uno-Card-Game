package model;

import java.util.Map;

/* --- JUno ------------------------------- */

import events.EventListener;
import events.EventManager;

import model.players.UserData;

import view.CUView;

public class CUModel extends EventManager implements EventListener {
    /* --- Singleton -------------------------- */

    private static CUModel instance;

    
    /** 
     * @return CUModel
     */
    public static CUModel getInstance() {
        if (instance == null)
            instance = new CUModel();
        return instance;
    }

    private CUModel() {
        subscribe(UserData.getEventListener(), "INFO_CHANGE", "INFO_RESET");
    }

    /* --- Fields ----------------------------- */

    /**
     * The CUView.
     */
    private static CUView receiverCU = CUView.getInstance();

    /* --- Body ------------------------------- */

    /**
     * Notifies the receiver CU (i.e., the CUView).
     * 
     * @param event The type of event to notify.
     * @param data  The data associatd with the event.
     */
    public static void communicate(String event, Map<String, Object> data) {
        receiverCU.update(event, data);
    }

    
    /** 
     * @param event
     * @param data
     */
    /* --- Observer --------------------------- */

    @Override
    public void update(String event, Map<String, Object> data) {
        notify(event, data);
    }
}
