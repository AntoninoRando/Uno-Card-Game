package model.gameObjects;

import model.gameEntities.Player;
import model.gameLogic.Game;

public class ReverseCard extends Card {
    public ReverseCard(Suit suit, int value) {
        super(suit, value);

    }

    @Override
    public void play() {
        int n = Game.countPlayers();
        Player[] oldOrder = Game.getPlayers();
        Player[] newOrder = new Player[n];

        int currentTurn = Game.getTurn();
        for (int i = 0; i < n; i++)
            newOrder[(i + currentTurn) % n] = oldOrder[((currentTurn - i) + n) % n];

        Game.setTurnOrder(newOrder);
    }
}
