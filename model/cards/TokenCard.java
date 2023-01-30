package model.cards;

import java.util.Collection;

/**
 * A card with just a value and a suit that can be used in specific situations.
 */
public class TokenCard extends Card {
    public TokenCard(Suit suit, int value) {
        super(suit, value);
    }

    /**
     * It does nothing.
     */
    @Override
    public void play() {
    }

    /**
     * It does not reshuffle.
     */
    @Override
    public void shuffleIn(Collection<Card> cards) {
    }

}
