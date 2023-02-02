package model.cards;

import java.util.List;

import model.gameLogic.Game;
import model.players.Player;

/**
 * Pick a card between multiple cards options. This class is abstract, thus the
 * cards options, and the effect after the card has been selected, must be
 * implemented by another class.
 */
public abstract class PickCard extends Card {
    protected Card[] options;
    protected Card choice;

    public PickCard(Suit suit, int value) {
        super(suit, value);
    }

    @Override
    public void play(Game game) {
        Player source = game.getCurrentPlayer();
        choice = (Card) source.chooseFrom(List.of(options)).getValue();
    }
}
