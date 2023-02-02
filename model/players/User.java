package model.players;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import events.Event;

/* --- JUno ------------------------------- */

import events.EventListener;
import model.CUModel;
import model.cards.Card;
import model.gameLogic.Action;

public class User extends Player implements EventListener {
    /* --- Singleton -------------------------- */

    private static User instance;

    public static User getInstance() {
        if (instance == null)
            instance = new User();
        return instance;
    }

    private User() {
        super(UserData.getIcon(), UserData.getNickname());
        CUModel.getInstance().subscribe(this, Event.TURN_DECISION, Event.SELECTION);
    }

    /* --- Fields ----------------------------- */

    private Entry<Action, Object> choice;
    private HashMap<Integer, Card> selectionCards;

    /* --- Player ----------------------------- */

    @Override
    public Entry<Action, Object> chooseFrom(Collection<Card> cards) {
        choice = null;

        selectionCards = (HashMap<Integer, Card>) cards.stream().collect(Collectors.toMap(
                card -> card.getTag(),
                card -> card));

        HashMap<String, Object> data = new HashMap<>();
        data.put("all-card-tags", cards.stream().mapToInt(card -> card.getTag()).toArray());
        data.put("all-card-representations", cards.stream().map(card -> card.toString()).toArray(String[]::new));
        CUModel.communicate(Event.USER_SELECTING_CARD, data);

        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }

        int tag = (int) choice.getValue();
        Card card = selectionCards.get(tag);
        return Map.entry(Action.SELECTION_COMPLETED, card);
    }

    @Override
    public Entry<Action, Object> chooseFrom(Collection<Card> cards, Predicate<Card> validate) {
        choice = null;

        // Waits for the user input.
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Decode the user input.
        switch (choice.getKey()) {
            case FROM_DECK_DRAW:
            case SAY_UNO:
            case SKIP:
                return choice;
            case FROM_HAND_PLAY_TAG:
                int tag = (int) choice.getValue();
                Card card = getHand().stream().filter(c -> c.getTag() == tag).findAny().orElseThrow();
                return validate.test(card) ? Map.entry(Action.FROM_HAND_PLAY_CARD, card)
                        : Map.entry(Action.INVALID, card);
            default:
                throw new Error("User turn resolved with an unimplemented choice: " + choice.getKey());
        }

    }

    /* --- Observer --------------------------- */

    @Override
    public void update(events.Event event, HashMap<String, Object> data) {
        Action action = Action.valueOf((String) data.get("choice-type"));
        Object info = data.get("choice");

        switch (event) {
            case TURN_DECISION:
                if (getState() == 1) {
                    synchronized (this) {
                        choice = Map.entry(action, info);
                        notify();
                    }
                    break;
                }

                if (action.equals(Action.FROM_HAND_PLAY_TAG)) {
                    data.put("card-tag", info);
                    CUModel.communicate(Event.INVALID_CARD, data);
                }
                break;
            case SELECTION:
                CUModel.communicate(Event.SELECTION, null);
                synchronized (this) {
                    choice = Map.entry(action, info);
                    notify();
                }
                break;
            default:
                throwUnsupportedError(event, data);
        }

    }

}
