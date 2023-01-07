package model.gameLogic;

/**
 * Implements the <em>State Pattern</em>, together with <code>AITurn</code>,
 * <code>UserTurn</code> and <code>UserSelection</code> classes (states).
 */
public interface GameState {
    /**
     * Resolve the turn accordingly to this game state.
     */
    public void resolve();
}
