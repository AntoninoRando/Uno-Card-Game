package model.gameLogic;

/**
 * Implements the <em>State Pattern</em>.
 */
public interface GameState {
    /**
     * Resolve the turn accordingly to this game state.
     */
    public void resolve();
}
