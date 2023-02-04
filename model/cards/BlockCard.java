package model.cards;

/* --- JUno ------------------------------- */

import events.Event;

import model.gameLogic.Game;
import model.players.Player;

/**
 * Skips the next player turn.
 */
public class BlockCard extends Card {
    public BlockCard(Suit suit, int value) {
        super(suit, value);
    }

    @Override
    public void play(Game game) {
        // Advance turn
        int newTurn = (game.getTurn() + 1) % game.getPlayers().length;
        game.setTurn(newTurn);
        Player blocked = game.getCurrentPlayer();
        game.notifyToCU(Event.TURN_BLOCKED, blocked.getData());
    }
}
