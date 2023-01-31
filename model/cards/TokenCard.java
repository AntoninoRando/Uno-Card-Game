package model.cards;

import java.util.Collection;

/**
 * A simple card that will not reshuffle in the deck.
 */
public class TokenCard extends SimpleCard {
    public TokenCard(Suit suit, int value) {
        super(suit, value);
    }

    /**
     * It does not reshuffle.
     */
    @Override
    public void shuffleIn(Collection<Card> cards) {
    }

}
