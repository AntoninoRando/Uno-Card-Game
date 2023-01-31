package model.gameLogic;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import events.EventListener;
import events.Event;
import model.cards.Card;
import model.players.GameAI;
import model.players.Player;

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

        boolean unoNeed = false;
        if (user.getHand().size() == 2)
            unoNeed = true;

        game.notifyToCU(Event.TURN_START, user.getData());

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
                    game.dealFromDeck(user, quantity);
                    endTurn = true;
                    break;
                case FROM_HAND_PLAY_TAG:
                    int tag = (int) choice.getValue();
                    Card card = user.getHand().stream().filter(c -> c.getTag() == tag).findAny().orElseThrow();

                    if (!game.getPlayCondition().test(card)) {
                        HashMap<String, Object> data = new HashMap<>();
                        data.put("card-tag", card.getTag());
                        game.notifyToCU(Event.INVALID_CARD, data);
                        break;
                    }

                    cardPlayed = Optional.of(card);
                    game.changeCurrentCard(card);
                    user.getHand().remove(card);

                    HashMap<String, Object> data = user.getData();
                    data.putAll(card.getData());
                    game.notifyToCU(Event.USER_PLAYED_CARD, data);
                    game.notifyToCU(Event.CARD_CHANGE, data);
                    endTurn = true;

                    if (unoNeed) {
                        HashMap<String, Object> data2 = new HashMap<>();
                        data2.put("said", false);
                        game.notifyToCU(Event.UNO_DECLARED, data2);
                        game.dealFromDeck(user, 2);
                    }

                    break;
                case SAY_UNO:
                    if (!unoNeed)
                        break;
                    unoNeed = false;
                    HashMap<String, Object> data2 = new HashMap<>();
                    data2.put("said", true);
                    game.notifyToCU(Event.UNO_DECLARED, data2);
                    break;
                case SKIP:
                    return;
                default:
                    throw new Error("User turn resolved with an unimplemented choice: " + choice.getKey());
            }
        }
    }

    public void passTurn() {
        if (cardPlayed.isPresent()) {
            CardTurn nextState = new CardTurn();
            nextState.setContext(cardPlayed.get(), game);
            game.changeState(nextState);
        } else {
            TransitionState nextState = new TransitionState();
            nextState.setContext(game);
            game.changeState(nextState);
        }
    }

    public Card chooseFrom(Card... cards) {
        selectionCards = (HashMap<Integer, Card>) Stream.of(cards).collect(Collectors.toMap(
                card -> card.getTag(),
                card -> card));

        HashMap<String, Object> data = new HashMap<>();
        data.put("all-card-tags", Stream.of(cards).mapToInt(card -> card.getTag()).toArray());
        data.put("all-card-representations", Stream.of(cards).map(card -> card.toString()).toArray(String[]::new));
        game.notifyToCU(Event.USER_SELECTING_CARD, data);

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

    public void setContext(Player user, Game game) {
        this.user = user;
        this.game = game;
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
        Object info = data.get("choice");

        switch (event) {
            case TURN_DECISION:
                if (!(game.getCurrentPlayer() instanceof GameAI)) {
                    synchronized (this) {
                        choice = Map.entry(action, info);
                        notify();
                    }
                    break;
                }

                if (action.equals(Action.FROM_HAND_PLAY_TAG)) {
                    data.put("card-tag", info);
                    game.notifyToCU(Event.INVALID_CARD, data);
                }
                break;
            case SELECTION:
                game.notifyToCU(Event.SELECTION, null);
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
