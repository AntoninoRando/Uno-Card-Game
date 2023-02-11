package model.cards;

import model.gameLogic.Game;
import model.players.Player;

/**
 * Reverse the current turn order.
 */
public class ReverseCard extends Card {
    public ReverseCard(Suit suit, int value) {
        super(suit, value);

    }

    
    /** 
     * @param game
     */
    @Override
    public void play(Game game) {
        Player[] oldOrder = game.getPlayers();
        int n = oldOrder.length;
        Player[] newOrder = new Player[n];

        int currentTurn = game.getTurn();
        for (int i = 0; i < n; i++)
            newOrder[(i + currentTurn) % n] = oldOrder[((currentTurn - i) + n) % n];

        game.setTurnOrder(newOrder);
    }
}
