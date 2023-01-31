package model.cards;

import java.util.List;

import events.Event;
import model.gameLogic.Game;
import model.gameLogic.UserTurn;
import model.players.GameAI;
import model.players.Player;

public class DrawAndColor extends DrawCard {
    protected Card[] options;
    protected Card choice;

    public DrawAndColor(Suit suit, int value, int quantity) {
        super(suit, value, quantity);
        options = new Card[4];
        for (int i = 0; i < 4; i++) {
            options[i] = new TokenCard(Suit.values()[i], -5);
        }
    }

    @Override
    public void play(Game game) {
        Player source = game.getCurrentPlayer();
        if (source instanceof GameAI)
            choice = (Card) (((GameAI) source).chooseFrom(List.of(options)).getValue());
        else
            choice = UserTurn.getInstance().chooseFrom(options);
        game.changeCurrentCard(choice);
        game.notifyToCU(Event.CARD_CHANGE, choice.getData());

        super.play(game);

    }
}
