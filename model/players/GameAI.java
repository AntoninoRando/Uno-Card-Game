package model.players;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.function.Predicate;

/* --- JUno ------------------------------- */

import model.cards.Card;
import model.gameLogic.Action;

/**
 * Implements the <em>Template Method Pattern</em>.
 * <p>
 * An abstract AI player. The concrete AI must define how to choose a card among
 * different options.
 */
public abstract class GameAI extends Player {
    public GameAI(String icon, String nickname) {
        super(icon, nickname);
    }

    /**
     * Given different cards options, returns the type of action to perform and the
     * value choosen (if there is any).
     * 
     * @param cards The cards options.
     * @return The type of action to perform associated with its info.
     */
    public abstract Entry<Action, Object> chooseFrom(Collection<Card> cards);

    /**
     * It has the same effect as the
     * <code>chooseFrom(Collection<Card> cards)</code>, but only the cards that
     * respect the validate conditions can be choosed.
     * 
     * @param cards    The cards options.
     * @param validate The filter to reduce the card options only to the valid
     *                 cards.
     * @return The type of action to perform associated with its info.
     */
    public abstract Entry<Action, Object> chooseFrom(Collection<Card> cards, Predicate<Card> validate);
}
