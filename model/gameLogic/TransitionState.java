package model.gameLogic;

import events.Event;
import model.players.GameAI;
import model.players.Player;

public class TransitionState implements GameState {
    /* --- State ------------------------------ */

    private Game game;

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
