package GameTools;

import CardsTools.Card;

public abstract class Controller {
    // !Visbilità default per usarle nel package
    Player bringer;
    GameController game;

    // CONSTRUCTORS !Non so se vada fatto

    // METHODS
    public abstract int[] getPlay(); // !Usato per prendere la giocata

    public abstract void makePlay(); // !Usato per performarla

    public void drawFromDeck() {
        bringer.addCard(game.dealFromDeck(0));
    }

    public void drawFromDeck(int times) {
        while (times-- > 0)
            drawFromDeck();
    }

    public boolean playCard(int cardPosition) {
        Card card = bringer.getCard(cardPosition);

        if (!game.isPlayable(card)) {
            return false;
        }

        game.changeCurrentCard(bringer.removeCard(cardPosition));
        return true;
    }

    // GETTERS AND SETTERS
    public Player getBringer() {
        return bringer;
    }

    public GameController getGame() {
        return game;
    }

    public void setGame(GameController game) {
        this.game = game;
    }
}