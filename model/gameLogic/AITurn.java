package model.gameLogic;

import java.util.HashMap;
import java.util.Map.Entry;

import events.EventType;
import model.CUModel;
import model.gameEntities.GameAI;
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
    private Game game; // TODO togliere la classe Actions e usare solo il game

    public void setContext(Game game, GameAI AI) {
        this.game = game;
        this.AI = AI;
    }

    @Override
    public void resolveTurn() {
        Entry<Action, Object> choice = AI.takeTurn();

        switch (choice.getKey()) {
            case FROM_DECK_DRAW:
                int quantity = (int) choice.getValue();
                Actions.dealFromDeck(AI, quantity);
                break;
            case FROM_HAND_PLAY_CARD:
                Card card = (Card) choice.getValue();
                Actions.changeCurrentCard(card);
                AI.getHand().remove(card);
                card.getEffect().ifPresent(effect -> effect.cast(AI, card));

                HashMap<String, Object> data = AI.getData();
                data.putAll(card.getData());
                CUModel.getInstance().communicate(EventType.CARD_CHANGE, data);
                CUModel.getInstance().communicate(EventType.PLAYER_PLAYED_CARD, data);
                CUModel.getInstance().communicate(EventType.PLAYER_HAND_DECREASE, data);
                break;
            default:
                throw new Error("AI toke its turn with an unimplemented choice: " + choice.getKey());
        }
    }
}
