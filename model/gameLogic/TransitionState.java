package model.gameLogic;

import events.Event;
import model.CUModel;
import model.players.GameAI;
import model.players.Player;

public class TransitionState implements GameState {
    /* --- Singleton -------------------------- */

    private static TransitionState instance;

    public static TransitionState getInstance() {
        if (instance == null)
            instance = new TransitionState();
        return instance;
    }

    private TransitionState() {
    }

    /* --- State ------------------------------ */

    @Override
    public void resolve() {
        Player oldPlayer = Game.getCurrentPlayer();
        CUModel.communicate(Event.TURN_END, oldPlayer.getData());

        Actions.advanceTurn(1);
        Player following = Game.getCurrentPlayer();

        /*
         * "In the State pattern, the particular states may be aware of each other and initiate transitions from one state to another [...]"
         */
        if (following instanceof GameAI) {
            AITurn nextState = new AITurn();
            nextState.setContext((GameAI) following);
            Game.changeState(nextState);
        } else
            Game.changeState(UserTurn.getInstance());

    }

}
