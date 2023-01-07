package model.gameLogic;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
    private Optional<Card> cardPlayed;

    /* --- Body ------------------------------- */

    public void playCard() {
        choice = null;
        cardPlayed = Optional.empty();
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
                        CUModel.communicate(EventType.INVALID_CARD, data);
                        break;
                    }

                    cardPlayed = Optional.of(card);
                    Actions.changeCurrentCard(card);
                    user.getHand().remove(card);

                    HashMap<String, Object> data = user.getData();
                    data.putAll(card.getData());
                    CUModel.communicate(EventType.CARD_CHANGE, data);
                    CUModel.communicate(EventType.PLAYER_PLAYED_CARD, data);
                    CUModel.communicate(EventType.PLAYER_HAND_DECREASE, data);
                    CUModel.communicate(EventType.USER_PLAYED_CARD, data);
                    endTurn = true;
                    break;
                // TODO case SAY_UNO:
                default:
                    throw new Error("User toke its turn with an unimplemented choice: " + choice.getKey());
            }
        }
    }

    public void passTurn() {
        if (cardPlayed.isPresent()) {
            game.changeState(new CardTurn(game, cardPlayed.get()));
            return;
        }

        Player following = game.getNextPlayer();
        game.setTurn(following);
        game.changeState(AITurn.getInstance(following.getNickame()));
    }

    /* --- State ------------------------------ */

    public void setContext(Game game, Player user) {
        this.game = game;
        this.user = user;
    }

    @Override
    public void resolve() {
        playCard();
        passTurn();
    }

    /* --- Observer --------------------------- */

    @Override
    public void update(EventType event, HashMap<String, Object> data) {
        switch (event) {
            case TURN_DECISION:
                Action action = Action.valueOf((String) data.get("choice-type"));

                if (game.getCurrentPlayer() instanceof GameAI) {
                    if (action.equals(Action.FROM_HAND_PLAY_TAG))
                        CUModel.communicate(EventType.INVALID_CARD, data);
                    break;
                }

                synchronized (this) {
                    choice = Map.entry(action, data.get("choice"));
                    notify();
                }
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }
}
