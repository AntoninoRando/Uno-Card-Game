package model.gameLogic;

/* --- JUno ------------------------------- */

import events.Event;

import model.players.GameAI;
import model.players.Player;

/**
 * The state in which the turn of one players ends and the turn of the following
 * player starts. After that, there will be either the AITurn state or the
 * UserTurn state.
 */
public class TransitionState implements GameState {
    /**
     * The context.
     */
    private Game game;

    /**
     * Sets the context to execute this state properly.
     * 
     * @param game The current game.
     */
    public void setContext(Game game) {
        this.game = game;
    }

    @Override
    public void resolve() {
        Player oldPlayer = game.getCurrentPlayer();
        game.notifyToCU(Event.TURN_END, oldPlayer.getData());

        game.advanceTurn(1);
        Player following = game.getCurrentPlayer();

        /*
         * "In the State pattern, the particular states may be aware of each other and initiate transitions from one state to another [...]"
         */
        if (following instanceof GameAI) {
            AITurn nextState = new AITurn();
            nextState.setContext((GameAI) following, game);
            game.changeState(nextState);
        } else
            game.changeState(UserTurn.getInstance());

    }

}
