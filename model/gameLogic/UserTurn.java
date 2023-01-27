package model.gameLogic;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import events.EventListener;
import events.Event;
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
    private HashMap<Integer, Card> selectionCards;

    /* --- Body ------------------------------- */

    public void takeTurn() {
        choice = null;
        cardPlayed = Optional.empty();
        boolean endTurn = false;

        CUModel.communicate(Event.TURN_START, user.getData());

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
                        CUModel.communicate(Event.INVALID_CARD, data);
                        break;
                    }

                    cardPlayed = Optional.of(card);
                    Actions.changeCurrentCard(card);
                    user.getHand().remove(card);

                    HashMap<String, Object> data = user.getData();
                    data.putAll(card.getData());
                    CUModel.communicate(Event.CARD_CHANGE, data);
                    CUModel.communicate(Event.PLAYER_PLAYED_CARD, data);
                    CUModel.communicate(Event.PLAYER_HAND_DECREASE, data);
                    CUModel.communicate(Event.USER_PLAYED_CARD, data);
                    endTurn = true;
                    break;
                // TODO case SAY_UNO:
                case SKIP:
                    return;
                default:
                    throw new Error("User turn resolved with an unimplemented choice: " + choice.getKey());
            }
        }
    }

    public void passTurn() {
        if (cardPlayed.isPresent()) {
            game.changeState(new CardTurn(game, cardPlayed.get()));
            return;
        }

        Player following = game.getNextPlayer();
        game.advanceTurn(1);
        game.changeState(AITurn.getInstance(following.getNickame()));
    }

    public Card chooseFrom(Card... cards) {
        selectionCards = (HashMap<Integer, Card>) Stream.of(cards).collect(Collectors.toMap(
                card -> card.getTag(),
                card -> card));

        HashMap<String, Object> data = new HashMap<>();
        data.put("all-card-tags", Stream.of(cards).mapToInt(card -> card.getTag()).toArray());
        data.put("all-card-representations", Stream.of(cards).map(card -> card.toString()).toArray(String[]::new));
        CUModel.communicate(Event.USER_SELECTING_CARD, data);

        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }

        int tag = (int) choice.getValue();
        Card card = selectionCards.get(tag);
        choice = null;
        selectionCards = null;
        return card;
    }

    /* --- State ------------------------------ */

    public void setContext(Game game, Player user) {
        this.game = game;
        this.user = user;
    }

    @Override
    public void resolve() {
        takeTurn();
        passTurn();
    }

    /* --- Observer --------------------------- */

    @Override
    public void update(Event event, HashMap<String, Object> data) {
        Action action = Action.valueOf((String) data.get("choice-type"));
        switch (event) {
            case TURN_DECISION:
                if (game.getCurrentPlayer() instanceof GameAI) {
                    if (action.equals(Action.FROM_HAND_PLAY_TAG))
                        CUModel.communicate(Event.INVALID_CARD, data);
                    break;
                }

                synchronized (this) {
                    choice = Map.entry(action, data.get("choice"));
                    notify();
                }
                break;
            case SELECTION:
                CUModel.communicate(Event.SELECTION, null);
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
