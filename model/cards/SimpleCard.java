package model.cards;

import model.gameLogic.Game;

/**
 * A card with just a suit and a value, but no effect.
 */
public class SimpleCard extends Card {
    public SimpleCard(Suit suit, int value) {
        super(suit, value);
    }

    /**
     * It does nothing.
     */
    @Override
    public void play(Game game) {
    }

}
