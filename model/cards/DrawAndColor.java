package model.cards;

/* --- JUno ------------------------------- */

import events.Event;

import model.gameLogic.Game;
import model.players.Player;

/**
 * Pick a card that is just a new color, then drops it on the ground. After
 * that, skips the next player turn.
 */
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
        choice = (Card) source.chooseFrom(options).getValue();
        game.changeCurrentCard(choice);
        game.notifyToCU(Event.CARD_CHANGE, choice.getData());

        super.play(game);

    }
}
