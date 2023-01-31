package model.players;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.function.Predicate;

import model.cards.Card;
import model.gameLogic.Action;

/**
 * Implements the <em>Template Method Pattern</em>.
 */
public abstract class GameAI extends Player {
    public GameAI(String icon, String nickname) {
        super(icon, nickname);
    }

    public abstract Entry<Action, Object> chooseFrom(Collection<Card> cards);

    public abstract Entry<Action, Object> chooseFrom(Collection<Card> cards, Predicate<Card> validate);
}
