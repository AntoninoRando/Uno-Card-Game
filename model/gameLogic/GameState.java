package model.gameLogic;

/**
 * Implements the <em>State Pattern</em>.
 * <p>
 * Functional interface that represents a game state and its operations. Its
 * functional method is <code>resolve</code>.
 */
@FunctionalInterface
public interface GameState {
    /**
     * Executes the operations that concern this game state.
     */
    public void resolve();
}
