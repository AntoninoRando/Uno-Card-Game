package model.cards;

import events.Event;
import model.gameLogic.Game;

public class ChoseColor extends PickColor {
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
        game.notifyToCU(Event.CARD_CHANGE, choice.getData());
    }
}
