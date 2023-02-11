package model.cards;

/* --- JUno ------------------------------- */

import model.gameLogic.Game;

/**
 * Pick a card that is just a new color, then drops it on the ground. After
 * that, skips the next player turn.
 */
public class DrawAndColor extends DrawCard {
    public DrawAndColor(Suit suit, int value, int quantity) {
        super(suit, value, quantity);
    }

    
    /** 
     * @param game
     */
    @Override
    public void play(Game game) {
        new ChoseColor(null, 0).play(game);
        super.play(game);
    }
}
