package view;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.IntStream;

import controller.CUController;
import events.EventListener;
import events.EventManager;
import events.Event;
import view.gameElements.Card;
import view.gameElements.ActionsChronology;
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

    /* --- Fields ----------------------------- */

    private static CUController receiverCU = CUController.getInstance();

    /* --- Body ------------------------------- */

    /**
     * Subscribe all model-changes listeners to this. Thos listeners will be
     * notified by this after a <code>CUModel.communicate</code> call.
     */
    private void subscribeAll() {
        subscribe(ActionsChronology.getInstance(), Event.AI_PLAYED_CARD, Event.USER_PLAYED_CARD, Event.GAME_READY);
        subscribe(SelectionPane.getInstance(), Event.USER_SELECTING_CARD, Event.SELECTION);
        subscribe(HandPane.getInstance(), Event.GAME_READY, Event.USER_PLAYED_CARD, Event.USER_DREW, Event.GAME_READY);
        subscribe(PlayerPane.getInstance(), Event.GAME_READY, Event.AI_PLAYED_CARD,
                Event.AI_DREW, Event.GAME_READY, Event.TURN_START, Event.USER_DREW, Event.USER_PLAYED_CARD);
        subscribe(TerrainPane.getInstance(), Event.CARD_CHANGE);
        subscribe(ProfileMenu.getInstance(), Event.INFO_CHANGE);
        subscribe(GameResults.getInstance(), Event.PLAYER_WON, Event.INFO_CHANGE);
    }

    private Entry<Integer, Card> getCard(HashMap<String, Object> data) {
        if (data == null || !data.containsKey("card-tag"))
            return null;

        int cardTag = (int) data.get("card-tag");
        Card node = Card.cards.get(cardTag);

        if (node != null)
            return Map.entry(cardTag, node);

        if (!data.containsKey("card-representation"))
            throw new Error("An EventListener needed a card, but no info were given.\nData given:" + data.toString());

        String cardRepr = (String) data.get("card-representation");
        node = new Card(cardTag, cardRepr);
        Card.cards.put(cardTag, node);

        return Map.entry(cardTag, node);

    }

    /* --- Observer --------------------------- */

    @Override
    public void update(Event event, HashMap<String, Object> data) {
        HashMap<String, Object> decodedData = data;

        Entry<Integer, Card> card = getCard(data);
        if (card != null) {
            Card.cards.putIfAbsent(card.getKey(), card.getValue());
            decodedData.put("card-node", card.getValue());
        }

        if (data != null && data.containsKey("all-card-tags")) {
            int[] cardTags = (int[]) data.get("all-card-tags");
            String[] cardReprs = (String[]) data.get("all-card-representations");
            for (int i = 0; i < cardTags.length; i++)
                Card.cards.putIfAbsent(cardTags[i], new Card(cardTags[i], cardReprs[i]));
            decodedData.put("all-card-nodes",
                    IntStream.of(cardTags).mapToObj(tag -> (Card) Card.cards.get((int) tag)).toArray(Card[]::new));
        }

        switch (event) {
            default:
                this.notify(event, decodedData);
        }
    }

    public static void communicate(Event event, HashMap<String, Object> data) {
        receiverCU.update(event, data);
    }
}
