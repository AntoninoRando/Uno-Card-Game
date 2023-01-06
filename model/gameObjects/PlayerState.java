package model.gameObjects;

import java.util.Map.Entry;

/**
 * Implements the <em>State Pattern</em>, together with <code>Playing</code>,
 * <code>Waiting</code> and <code>Deciding</code> classes (states).
 */
public interface PlayerState {
    /**
     * Decide the action to perform during the turn.
     * 
     * @return And entry that associate the choice type to the actual choice.
     */
    public Entry<String, Object> choose();
}
