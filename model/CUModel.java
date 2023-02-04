package model;

import java.util.Map;

/* --- JUno ------------------------------- */

import events.EventListener;
import events.EventManager;

import model.players.UserData;

import view.CUView;

/**
 * <b>C</b>ontrol <b>U</b>nit <b>View</b>. This class implements the
 * <em>observer-observable</em> patterns, playing the role of both
 * <code>EventManager</code> and <code>EventListener</code>.
 * <p>
 * This class communicates with the other Control Units (i.e.,
 * <code>CUModel</code> and <code>CUView</code>) and with the view components.
 * In this way, any view component communicates only with other componens within
 * the same package: the <code>CUView</code> is the intermediary for the view
 * and the external word. (But the communication with the external word is
 * performed only through the communication with <code>CUModel</code> and
 * <code>CUView</code> classes.) Thus, the <code>CUView</code> implements the
 * <em>MVC pattern</em> and the <em>Island framework</em>.
 * <p>
 * For example, the <code>Chronology</code> class saves as a memory content a
 * generic <code>Node</code>, not the specific <code>Card</code>. The
 * <code>Chronology</code> class transcends the implementation of other
 * classes as much as possible (<em>Island framework</em>). To decide the
 * particular memory to store inside the <code>Chronology</code>,
 * (e.g., a <code>Card</code>) will be this class, upon a notification from the
 * <code>CUModel</code> (<em>MVC</em> and <em>observer-observable</em>).
 * <p>
 * Another example, model elements can send their data as their object (e.g., ad
 * model card). These model elements doesn't know how the view elements
 * implemented the cards, but the CUModel knows it, so before sending the data
 * to the CUView, the CUModel change it in the view language (tags, nodes,
 * etc.). If the view change, only the CUView, CUModel and CUController should
 * chnage.
 */
public class CUModel extends EventManager implements EventListener {
    /* --- Singleton -------------------------- */

    private static CUModel instance;

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

    /* --- Observer --------------------------- */

    @Override
    public void update(String event, Map<String, Object> data) {
        notify(event, data);
    }
}
