package model.cards;

/* --- JUno ------------------------------- */

import model.gameLogic.Game;

/**
 * Skips the next player turn and makes them draw cards from deck.
 */
public class DrawCard extends BlockCard {
    private int quantity;

    public DrawCard(Suit suit, int value, int quantity) {
        super(suit, value);
        this.quantity = quantity;
    }

    @Override
    public void play(Game game) {
        super.play(game);
        game.dealFromDeck(game.getCurrentPlayer(), quantity);
    }
}
