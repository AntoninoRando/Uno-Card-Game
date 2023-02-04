package model.players;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

/* --- JUno ------------------------------- */

import events.EventListener;

import model.CUModel;
import model.cards.Card;

/**
 * A singleton instance of the Player representing the user. This player is able
 * to fetch the user input to perform actions on the game. Before the input is
 * given, this player is also able to make the game wait.
 */
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
        CUModel.getInstance().subscribe(this, "TURN_DECISION", "SELECTION");
    }

    /* --- Fields ----------------------------- */

    private Entry<String, Object> choice;

    /* --- Player ----------------------------- */

    @Override
    public Entry<String, Object> chooseFrom(Card[] cards) {
        choice = null;

        // We use as IDs the position in the selection.
        HashMap<String, Object> data = new HashMap<>();
        data.put("all-card-IDs", Arrays.stream(cards).mapToInt(card -> card.getTag()).toArray());
        data.put("all-card-representations", Arrays.stream(cards).map(card -> card.toString()).toArray(String[]::new));
        CUModel.communicate("USER_SELECTING_CARD", data);

        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }

        Object ID = choice.getValue();
        Card card = Arrays.stream(cards).filter(c -> ID.equals(c.getTag())).findAny().orElseThrow();
        return Map.entry("SELECTION_COMPLETED", card);
    }

    @Override
    public Entry<String, Object> chooseFrom(Card[] cards, Predicate<Card> validate) {
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
            case "FROM_DECK_DRAW":
            case "SAY_UNO":
                return choice;
            case "FROM_HAND_PLAY_TAG":
                Object ID = choice.getValue();
                Card card = getHand().stream().filter(c -> ID.equals(c.getTag())).findAny().orElseThrow();
                return validate.test(card) ? Map.entry("FROM_HAND_PLAY_CARD", card)
                        : Map.entry("INVALID", card);
            default:
                throw new Error("User turn resolved with an unimplemented choice: " + choice.getKey());
        }

    }

    /* --- Observer --------------------------- */

    @Override
    public void update(String event, Map<String, Object> data) {
        String action = (String) data.get("choice-type");
        Object info = data.get("choice");

        switch (event) {
            case "TURN_DECISION":
                // If it's not user turn, ignore choice.
                if (!isPlaying())
                    return;

                synchronized (this) {
                    choice = Map.entry(action, info);
                    notify();
                }
                break;
            case "SELECTION":
                CUModel.communicate("SELECTION", null);
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
