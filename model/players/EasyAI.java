package model.players;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Predicate;

/* --- JUno ------------------------------- */

import model.cards.Card;
import model.gameLogic.Action;

/**
 * The easiest form of AI player: plays the first valid card in hand; if there
 * isn't any, draws from deck.
 */
public class EasyAI extends GameAI {
    public EasyAI(String icon, String nickname) {
        super(icon, nickname);
    }

    @Override
    public Entry<Action, Object> chooseFrom(Collection<Card> cards, Predicate<Card> validate) {
        Entry<Action, Object> choice = Map.entry(Action.FROM_DECK_DRAW, 1);

        Optional<Card> option = cards.stream().filter(validate).findAny();
        if (option.isPresent())
            choice = Map.entry(Action.FROM_HAND_PLAY_CARD, option.get());

        return choice;
    }

    @Override
    public Entry<Action, Object> chooseFrom(Collection<Card> cards) {
        if (cards.isEmpty())
            return Map.entry(Action.FROM_DECK_DRAW, 1);
        return Map.entry(Action.FROM_HAND_PLAY_CARD, cards.stream().findAny().get());
    }
}
