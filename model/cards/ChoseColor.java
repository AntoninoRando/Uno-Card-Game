package model.cards;

/* --- JUno ------------------------------- */

import model.gameLogic.Game;

/**
 * Pick a card that is just a new color, then drops it on the ground.
 */
public class ChoseColor extends PickCard {
    public ChoseColor(Suit suit, int value) {
        super(suit, value);

        options = new Card[4];
        for (int i = 0; i < 4; i++) {
            options[i] = new TokenCard(Suit.values()[i], -5);
        }
    }

    @Override
    public void play(Game game) {
        super.play(game);
        game.changeCurrentCard(choice);
        game.notifyToCU("CARD_CHANGE", choice.getData());
    }
}
