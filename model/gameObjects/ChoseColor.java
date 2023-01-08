package model.gameObjects;

import events.Event;
import model.CUModel;
import model.gameLogic.Actions;

public class ChoseColor extends PickColor{
    public ChoseColor(Suit suit, int value) {
        super(suit, value);
        
        options = new Card[4];
        for (int i = 0; i < 4; i++) {
            options[i] = new TokenCard(Suit.values()[i], -5);
        }
    }
    
    @Override
    public void play() {
        super.play();
        Actions.changeCurrentCard(choice);
        CUModel.communicate(Event.CARD_CHANGE, choice.getData());
    }
}
