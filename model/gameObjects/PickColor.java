package model.gameObjects;

import model.gameEntities.GameAI;
import model.gameEntities.Player;
import model.gameLogic.Game;
import model.gameLogic.UserTurn;

public class PickColor extends Card {
    protected Card[] options;
    protected Card choice;

    public PickColor(Suit suit, int value) {
        super(suit, value);
    }

    @Override
    public void play() {
        Player source = Game.getInstance().getCurrentPlayer();

        if (source instanceof GameAI) 
            choice = (Card) (((GameAI) source).chooseFrom(new CardGroup(options)).getValue());
        else
            choice = UserTurn.getInstance().chooseFrom(options);
    }
}
