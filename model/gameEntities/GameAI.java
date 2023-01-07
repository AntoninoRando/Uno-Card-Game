package model.gameEntities;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.function.Predicate;

import model.gameLogic.Action;
import model.gameLogic.Game;
import model.gameObjects.Card;

/**
 * Implements the <em>Template Method Pattern</em>.
 */
public abstract class GameAI extends Player {
    public GameAI(String icon, String nickname) {
        super(icon, nickname);
    }
    
    public Entry<Action, Object> choose() {
        return chooseFrom(getHand(), Game.getInstance().getPlayCondition());
    }

    public abstract Entry<Action, Object> chooseFrom(Collection<Card> cards);

    public abstract Entry<Action, Object> chooseFrom(Collection<Card> cards, Predicate<Card> validate);
}
