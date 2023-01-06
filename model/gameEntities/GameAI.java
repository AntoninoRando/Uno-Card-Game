package model.gameEntities;

import java.util.Map.Entry;

import model.gameLogic.Action;

/**
 * Implements the <em>Template Method Pattern</em>.
 */
public abstract class GameAI extends Player {
    public GameAI(String icon, String nickname) {
        super(icon, nickname);
    }
    
    public Entry<Action, Object> takeTurn() {
        return chooseFromHand();
    }

    public abstract Entry<Action, Object> chooseFromHand();

    public abstract Entry<Action, Object> chooseFromSelection();
}
