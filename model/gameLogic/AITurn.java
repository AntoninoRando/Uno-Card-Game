package model.gameLogic;

import java.util.HashMap;
import java.util.Optional;
import java.util.Map.Entry;

import events.EventType;
import model.CUModel;
import model.gameEntities.GameAI;
import model.gameEntities.Player;
import model.gameObjects.Card;

public class AITurn implements GameState {
    /* --- Singleton -------------------------- */

    private static HashMap<String, AITurn> instances = new HashMap<>();

    public static AITurn getInstance(String AINickname) {
        instances.putIfAbsent(AINickname, new AITurn());
        return instances.get(AINickname);
    }

    private AITurn() {
    }

    /* --- Fields ----------------------------- */

    private GameAI AI;
    private Game game;
    private Optional<Card> cardPlayed;


    /* --- Body ------------------------------- */

    public void takeTurn() {
        cardPlayed = Optional.empty();

        CUModel.communicate(EventType.TURN_START, AI.getData());
        Entry<Action, Object> choice = AI.choose();

        switch (choice.getKey()) {
            case FROM_DECK_DRAW:
                int quantity = (int) choice.getValue();
                Actions.dealFromDeck(AI, quantity);
                break;
            case FROM_HAND_PLAY_CARD:
                Card card = (Card) choice.getValue();

                cardPlayed = Optional.of(card);
                Actions.changeCurrentCard(card);
                AI.getHand().remove(card);

                HashMap<String, Object> data = AI.getData();
                data.putAll(card.getData());
                CUModel.communicate(EventType.CARD_CHANGE, data);
                CUModel.communicate(EventType.PLAYER_PLAYED_CARD, data);
                CUModel.communicate(EventType.PLAYER_HAND_DECREASE, data);
                break;
            default:
                throw new Error("AI toke its turn with an unimplemented choice: " + choice.getKey());
        }
    }

    public void passTurn() {
        if (cardPlayed.isPresent()) {
            game.changeState(new CardTurn(game, cardPlayed.get()));
            return;
        }

        CUModel.communicate(EventType.TURN_END, AI.getData());

        Player following = game.getNextPlayer();
        game.setTurn(following);

        /*
         * "In the State pattern, the particular states may be aware of each other and initiate transitions from one state to another [...]"
         */
        if (following instanceof GameAI)
            game.changeState(getInstance(following.getNickame()));
        else
            game.changeState(UserTurn.getInstance());
    }

    /* --- State ------------------------------ */

    public void setContext(Game game, GameAI AI) {
        this.game = game;
        this.AI = AI;
    }

    @Override
    public void resolve() {
        takeTurn();
        passTurn();
    }
}
