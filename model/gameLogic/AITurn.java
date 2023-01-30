package model.gameLogic;

import java.util.HashMap;
import java.util.Map.Entry;

import events.Event;
import model.CUModel;
import model.gameEntities.GameAI;
import model.gameObjects.Card;

public class AITurn implements GameState {
    /* --- Fields ----------------------------- */

    private GameAI AI;

    /* --- State ------------------------------ */

    public void setContext(GameAI AI) {
        this.AI = AI;
    }

    @Override
    public void resolve() {
        Card cardPlayed = null;

        CUModel.communicate(Event.TURN_START, AI.getData());
        Entry<Action, Object> choice = AI.choose();

        switch (choice.getKey()) {
            case FROM_DECK_DRAW:
                int quantity = (int) choice.getValue();
                Actions.dealFromDeck(AI, quantity);
                break;
            case FROM_HAND_PLAY_CARD:
                cardPlayed = (Card) choice.getValue();
                Actions.changeCurrentCard(cardPlayed);
                AI.getHand().remove(cardPlayed);

                HashMap<String, Object> data = AI.getData();
                data.putAll(cardPlayed.getData());
                CUModel.communicate(Event.AI_PLAYED_CARD, data);
                CUModel.communicate(Event.CARD_CHANGE, data);
                break;
            default:
                throw new Error("AI toke its turn with an unimplemented choice: " + choice.getKey());
        }

        if (cardPlayed != null)
            Game.changeState(new CardTurn(cardPlayed));
        else
            Game.changeState(TransitionState.getInstance());
    }
}
