package view;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.IntStream;

/* --- JUno ------------------------------- */

import controller.CUController;

import events.EventListener;
import events.EventManager;

import view.gameElements.Card;

public class CUView extends EventManager implements EventListener {
    /* --- Singleton -------------------------- */

    private static CUView instance;

    
    /** 
     * @return CUView
     */
    public static CUView getInstance() {
        if (instance == null)
            instance = new CUView();
        return instance;
    }

    /* --- Fields ----------------------------- */

    private static CUController receiverCU = CUController.getInstance();

    
    /** 
     * @param event
     * @param data
     */
    /* --- Body ------------------------------- */

    public static void communicate(String event, Map<String, Object> data) {
        receiverCU.update(event, data);
    }

    private Entry<Integer, Card> getCard(Map<String, Object> data) {
        if (data == null || !data.containsKey("card-ID"))
            return null;

        int cardTag = (int) data.get("card-ID");
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
    public void update(String event, Map<String, Object> data) {
        Map<String, Object> decodedData = data;

        Entry<Integer, Card> card = getCard(data);
        if (card != null) {
            Card.cards.putIfAbsent(card.getKey(), card.getValue());
            decodedData.put("card-node", card.getValue());
        }

        if (data != null && data.containsKey("all-card-IDs")) {
            int[] cardTags = (int[]) data.get("all-card-IDs");
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
}
