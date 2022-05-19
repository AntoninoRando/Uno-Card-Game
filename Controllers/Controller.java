package Controllers;

import CardsTools.Card;
import GameTools.GameManager;

public abstract class Controller {
    protected Player bringer;
    protected GameManager game;

    // CONSTRUCTORS !Non so se vada fatto

    // METHODS
    public abstract int[] getPlay(); // !Usato per prendere la giocata

    public abstract void makePlay(); // !Usato per performarla

    public void drawFromDeck() {
        bringer.addCard(game.drawFromDeck());
    }

    public void drawFromDeck(int times) {
        while (times-- > 0)
            bringer.addCard(game.drawFromDeck());
    }

    // !Il metodo non è proprio buono perché prima prende la carta, poi controlla se
    // è giocabile (cosa che fa anche il gioco: ripetitivo) ed infine rimuove la
    // carta
    public boolean playCard(int cardPosition) {
        Card card = bringer.getCard(cardPosition);
        if (!card.isPlayable(game)) {
            return false;
        }
        game.putCard(bringer.removeCard(cardPosition));
        return true;
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