package model.players;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Predicate;

/* --- JUno ------------------------------- */

import model.cards.Card;

/**
 * The easiest form of AI player: plays the first valid card in hand; if there
 * isn't any, draws from deck.
 */
public class EasyAI extends Player {
    private boolean unoDeclared;

    public EasyAI(String icon, String nickname) {
        super(icon, nickname);
    }

    @Override
    public Entry<String, Object> chooseFrom(Card[] cards, Predicate<Card> validate) {
        Entry<String, Object> choice = Map.entry("FROM_DECK_DRAW", 1);

        Optional<Card> option = Arrays.stream(cards).filter(validate).findAny();
        if (option.isPresent()) {
            if (cards.length == 2 && !unoDeclared) {
                choice = Map.entry("SAY_UNO", 0);
                unoDeclared = true;
            } else {
                choice = Map.entry("FROM_HAND_PLAY_CARD", option.get());
                unoDeclared = false;
            }
        }

        return choice;
    }

    @Override
    public Entry<String, Object> chooseFrom(Card[] cards) {
        if (cards.length == 0)
            return Map.entry("FROM_DECK_DRAW", 1);
        return Map.entry("FROM_HAND_PLAY_CARD", cards[0]);
    }
}
