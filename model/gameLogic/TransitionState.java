package model.gameLogic;

/* --- JUno ------------------------------- */

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
        // Sets the player state to: Waiting.
        Player oldPlayer = game.getCurrentPlayer();
        oldPlayer.setPlayingState(false);
        game.notifyToCU("TURN_END", oldPlayer.getData());


        // Advance turn
        int newTurn = (game.getTurn() + 1) % game.getPlayers().length;
        game.setTurn(newTurn);

        // Sets the player state to: Playing.
        Player following = game.getCurrentPlayer();
        following.setPlayingState(true);

        /*
         * "In the State pattern, the particular states may be aware of each other and initiate transitions from one state to another [...]"
         */

        PlayerTurn nextState = new PlayerTurn();
        nextState.setContext(following, game);
        game.changeState(nextState);
    }

}
