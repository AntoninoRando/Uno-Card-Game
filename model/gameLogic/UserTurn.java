package model.gameLogic;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import events.EventListener;
import events.EventType;
import model.CUModel;
import model.gameEntities.GameAI;
import model.gameEntities.Player;
import model.gameObjects.Card;

public class UserTurn implements GameState, EventListener {
    /* --- Singleton -------------------------- */

    private static UserTurn instance;

    public static UserTurn getInstance() {
        if (instance == null)
            instance = new UserTurn();
        return instance;
    }

    private UserTurn() {
    }

    /* --- Fields ----------------------------- */

    private Player user;
    private Game game;
    private Entry<Action, Object> choice;

    public void setContext(Game game, Player user) {
        this.game = game;
        this.user = user;
    }

    @Override
    public void resolveTurn() {
        boolean endTurn = false;

        while (!endTurn) {
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }

            switch (choice.getKey()) {
                case FROM_DECK_DRAW:
                    int quantity = (int) choice.getValue();
                    Actions.dealFromDeck(user, quantity);
                    endTurn = true;
                    break;
                case FROM_HAND_PLAY_TAG:
                    int tag = (int) choice.getValue();
                    Card card = user.getHand().stream().filter(c -> c.getTag() == tag).findAny().orElseThrow();

                    if (!game.isPlayable(card)) {
                        HashMap<String, Object> data = new HashMap<>();
                        data.put("card-tag", card.getTag());
                        CUModel.getInstance().communicate(EventType.INVALID_CARD, data);
                        break;
                    }

                    Actions.changeCurrentCard(card);
                    user.getHand().remove(card);
                    card.getEffect().ifPresent(effect -> effect.cast(user, card));

                    HashMap<String, Object> data = user.getData();
                    data.putAll(card.getData());
                    CUModel.getInstance().communicate(EventType.CARD_CHANGE, data);
                    CUModel.getInstance().communicate(EventType.PLAYER_PLAYED_CARD, data);
                    CUModel.getInstance().communicate(EventType.PLAYER_HAND_DECREASE, data);
                    CUModel.getInstance().communicate(EventType.USER_PLAYED_CARD, data);
                    endTurn = true;
                    break;
                // TODO case SAY_UNO:
                default:
                    throw new Error("User toke its turn with an unimplemented choice: " + choice.getKey());
            }
        }

        choice = null;
    }

    @Override
    public void update(EventType event, HashMap<String, Object> data) {
        switch (event) {
            case TURN_DECISION:
                if (game.getCurrentPlayer() instanceof GameAI) {
                    if (data.containsKey("card-tag"))
                        CUModel.getInstance().communicate(EventType.INVALID_CARD, data);
                    break;
                }
                synchronized (this) {
                    Action action = Action.valueOf((String) data.get("choice-type"));
                    choice = Map.entry(action, data.get("choice"));
                    notify();
                }
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }
}
