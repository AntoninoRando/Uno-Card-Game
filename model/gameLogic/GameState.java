package model.gameLogic;

/**
 * Implements the <em>State Pattern</em>.
 * 
 * Functional interface that represents a game state and its operations.
 */
@FunctionalInterface
public interface GameState {
    /**
     * Executes the operations that concern this game state.
     */
    public void resolve();
}
