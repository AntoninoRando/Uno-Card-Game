package model.cards;

import model.gameLogic.Game;

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
