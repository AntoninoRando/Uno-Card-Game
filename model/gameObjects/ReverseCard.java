package model.gameObjects;

import model.gameEntities.Player;
import model.gameLogic.Game;

public class ReverseCard extends Card {
    public ReverseCard(Suit suit, int value) {
        super(suit, value);

    }

    @Override
    public void play() {
        Game game = Game.getInstance();

        int n = game.countPlayers();
        Player[] oldOrder = game.getTurnOrder();
        Player[] newOrder = new Player[n];

        int currentTurn = game.getTurn();
        for (int i = 0; i < n; i++)
            newOrder[(i + currentTurn) % n] = oldOrder[((currentTurn - i) + n) % n];

        game.setTurnOrder(newOrder);
    } 
}
