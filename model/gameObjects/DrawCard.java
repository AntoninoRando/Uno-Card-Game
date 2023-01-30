package model.gameObjects;

import model.gameLogic.Actions;
import model.gameLogic.Game;

public class DrawCard extends BlockCard {
    private int quantity;

    public DrawCard(Suit suit, int value, int quantity) {
        super(suit, value);
        this.quantity = quantity;
    }
    
    @Override
    public void play() {
        super.play();
        Actions.dealFromDeck(Game.getCurrentPlayer(), quantity);
    }
}
