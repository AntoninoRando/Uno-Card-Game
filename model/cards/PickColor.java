package model.cards;

import java.util.List;

import model.gameLogic.Game;
import model.gameLogic.UserTurn;
import model.players.GameAI;
import model.players.Player;

public class PickColor extends Card {
    protected Card[] options;
    protected Card choice;

    public PickColor(Suit suit, int value) {
        super(suit, value);
    }

    @Override
    public void play(Game game) {
        Player source = game.getCurrentPlayer();

        if (source instanceof GameAI) 
            choice = (Card) (((GameAI) source).chooseFrom(List.of(options)).getValue());
        else
            choice = UserTurn.getInstance().chooseFrom(options);
    }
}
