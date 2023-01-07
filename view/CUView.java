package view;

import java.util.HashMap;

import controller.CUController;
import events.EventListener;
import events.EventManager;
import events.Event;
import view.gameElements.Card;
import view.gameElements.CardChronology;
import view.gameElements.HandPane;
import view.gameElements.PlayerPane;
import view.gameElements.SelectionPane;
import view.gameElements.TerrainPane;
import view.settings.ProfileMenu;

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
 */
public class CUView extends EventManager implements EventListener {
    /* --- Singleton -------------------------- */

    private static CUView instance;

    public static CUView getInstance() {
        if (instance == null)
            instance = new CUView();
        return instance;
    }

    private CUView() {
        subscribeAll();
    }
    /* --- Field ------------------------------ */

    private static CUController receiverCU = CUController.getInstance();

    /* --- Body ------------------------------- */

    /**
     * Subscribe all model-changes listeners to this. Thos listeners will be
     * notified by this after a <code>CUModel.communicate</code> call.
     */
    private void subscribeAll() {
        subscribe(CardChronology.getInstance(), Event.PLAYER_PLAYED_CARD, Event.RESET);
        subscribe(SelectionPane.getInstance(), Event.USER_SELECTING_CARD);
        subscribe(HandPane.getInstance(), Event.GAME_READY, Event.USER_PLAYED_CARD, Event.USER_DREW);
        subscribe(PlayerPane.getInstance(), Event.GAME_READY, Event.PLAYER_HAND_DECREASE,
                Event.PLAYER_HAND_INCREASE);
        subscribe(TerrainPane.getInstance(), Event.GAME_READY, Event.CARD_CHANGE);
        subscribe(new Card(), Event.NEW_CARD);
        subscribe(ProfileMenu.getInstance(), Event.INFO_CHANGE);
        // subscribe(GameResults.getInstance(), EventType.PLAYER_WON);
    }

    /* --- Observer --------------------------- */

    @Override
    public void update(Event event, HashMap<String, Object> data) {
        HashMap<String, Object> decodedData = data;
        switch (event) {
            case PLAYER_PLAYED_CARD:
                int cardTag = (int) data.get("card-tag");
                String cardRepr = (String) data.get("card-representation");
                Card.cards.putIfAbsent(cardTag, new Card(cardTag, cardRepr));
                Card card = Card.cards.get((int) data.get("card-tag"));
                decodedData.put("card", card);
                this.notify(event, decodedData);
                break;
            case USER_SELECTING_CARD:
                int[] cardTags = (int[]) data.get("all-card-tags");
                String[] cardReprs = (String[]) data.get("all-card-representations");
                for (int i = 0; i < cardTags.length; i++)
                    Card.cards.putIfAbsent(cardTags[i], new Card(cardTags[i], cardReprs[i]));
                decodedData.remove("all-card-representations");
                this.notify(event, decodedData);
                break;
            default:
                this.notify(event, decodedData);
                break;
        }
    }

    public static void communicate(Event event, HashMap<String, Object> data) {
        receiverCU.update(event, data);
    }

    /*
     * TODO questa classe rischia di diventare enorme. Inoltre, in realtà ogni
     * classe del gameElements funziona a sé stante, bisogna solo togliere
     * l'implement di EventListener. Potrei pittosto usare il decorator per, as
     * esempio, creare la classe Cronologia, generale, e poi usare il decorator
     * CronologiaCarte. Anche se tanto vale usare un generic, ma in questo caso non
     * avrebbe senso il metodo update che gioca intorno all'evento
     * PLAYER_PLAYED_CARD.
     */
}
