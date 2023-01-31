package model.gameLogic;

import java.util.HashMap;
import java.util.Map.Entry;

/* --- JUno ------------------------------- */

import events.Event;

import model.cards.Card;
import model.players.GameAI;

/**
 * The game state in which the AI will resolve its turn. After that, there will
 * be either the TransitionState or the CardTurn state.
 */
public class AITurn implements GameState {
    /* --- State ------------------------------ */

    /**
     * The context.
     */
    private GameAI AI;
    /**
     * The context.
     */
    private Game game;

    /**
     * Sets the context for this state.
     * 
     * @param AI   The AI player that will take the turn.
     * @param game The current match in which the AI is taking its turn.
     */
    public void setContext(GameAI AI, Game game) {
        this.AI = AI;
        this.game = game;
    }

    @Override
    public void resolve() {
        Card cardPlayed = null;

        game.notifyToCU(Event.TURN_START, AI.getData());
        Entry<Action, Object> choice = AI.chooseFrom(AI.getHand(), game.getPlayCondition());

        switch (choice.getKey()) {
            case FROM_DECK_DRAW:
                int quantity = (int) choice.getValue();
                game.dealFromDeck(AI, quantity);
                break;
            case FROM_HAND_PLAY_CARD:
                cardPlayed = (Card) choice.getValue();
                game.changeCurrentCard(cardPlayed);
                AI.getHand().remove(cardPlayed);

                HashMap<String, Object> data = AI.getData();
                data.putAll(cardPlayed.getData());
                game.notifyToCU(Event.AI_PLAYED_CARD, data);
                game.notifyToCU(Event.CARD_CHANGE, data);
                break;
            default:
                throw new Error("AI toke its turn with an unimplemented choice: " + choice.getKey());
        }

        if (cardPlayed != null) {
            CardTurn nextState = new CardTurn();
            nextState.setContext(cardPlayed, game);
            game.changeState(nextState);
        } else {
            TransitionState nextState = new TransitionState();
            nextState.setContext(game);
            game.changeState(nextState);
        }
    }
}
