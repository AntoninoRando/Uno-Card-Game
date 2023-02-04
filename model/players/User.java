package model.players;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
/* --- JUno ------------------------------- */

import events.Event;
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

    /* --- Player ----------------------------- */

    @Override
    public Entry<Action, Object> chooseFrom(Card[] cards) {
        choice = null;

        // We use as IDs the position in the selection.
        HashMap<String, Object> data = new HashMap<>();
        data.put("all-card-IDs", Arrays.stream(cards).mapToInt(card -> card.getTag()).toArray());
        data.put("all-card-representations", Arrays.stream(cards).map(card -> card.toString()).toArray(String[]::new));
        CUModel.communicate(Event.USER_SELECTING_CARD, data);

        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }

        Object ID = choice.getValue();
        Card card = Arrays.stream(cards).filter(c -> ID.equals(c.getTag())).findAny().orElseThrow();
        return Map.entry(Action.SELECTION_COMPLETED, card);
    }

    @Override
    public Entry<Action, Object> chooseFrom(Card[] cards, Predicate<Card> validate) {
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
                return choice;
            case FROM_HAND_PLAY_TAG:
                Object ID = choice.getValue();
                Card card = getHand().stream().filter(c -> ID.equals(c.getTag())).findAny().orElseThrow();
                return validate.test(card) ? Map.entry(Action.FROM_HAND_PLAY_CARD, card)
                        : Map.entry(Action.INVALID, card);
            default:
                throw new Error("User turn resolved with an unimplemented choice: " + choice.getKey());
        }

    }

    /* --- Observer --------------------------- */

    @Override
    public void update(Event event, Map<String, Object> data) {
        Action action = Action.valueOf((String) data.get("choice-type"));
        Object info = data.get("choice");

        switch (event) {
            case TURN_DECISION:
                // If it's not user turn, ignore choice.
                if (getState() != 1)
                    return;

                synchronized (this) {
                    choice = Map.entry(action, info);
                    notify();
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
