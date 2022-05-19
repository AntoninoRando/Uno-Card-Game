package Controllers;

import GameTools.GameManager;
import GameTools.Player;

public abstract class Controller {
    protected Player bringer;
    protected GameManager game;

    // CONSTRUCTORS !Non so se vada fatto

    // METHODS
    public abstract int getPlay();

    public abstract void makePlay();

    public void drawFromDeck() {
        bringer.addCard(game.drawFromDeck());
    }

    public void drawFromDeck(int times) {
        while (times-- > 0)
            bringer.addCard(game.drawFromDeck());
    }

    // GETTERS AND SETTERS
    public Player getBringer() {
        return bringer;
    }

    public GameManager getGame() {
        return game;
    }

    public void setGame(GameManager game) {
        this.game = game;
    }
}