package model;

import CardsTools.Card;
import GUI.CardChangedListener;

public abstract class Controller {
    // !Visbilità default per usarle nel package
    Player bringer;
    GameController game;

    // LISTENERS
    // !Usato dal model per avvertire che il controller ha provato a giocare una
    // carta, e quindi si deve aggiornare il model.
    protected TryingToPlayCardListener tryingToPlayCardListener;
    // !Usato dalla view per avvertire che il controller ha una carta in meno nella
    // mano, e quindi si deve aggiornare la view.
    protected CardChangedListener cardListener;

    // CONSTRUCTORS !Non so se vada fatto

    // METHODS
    public abstract int[] getPlay() throws InterruptedException; // !Usato per prendere la giocata

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
        boolean haveCardBeenPlayed = tryingToPlayCardListener.tryToPlayCard(card);

        if (haveCardBeenPlayed)
            return false;

        bringer.removeCard(cardPosition);
        // !Questo cardListener va migliorato, perché è la view che ascolta il
        // cambiamento
        if (cardListener != null)
            cardListener.cardChanged();

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

    public void setCardListener(CardChangedListener cardListener) {
        this.cardListener = cardListener;
    }

    public void setTryingToPlayCardListener(TryingToPlayCardListener tryingToPlayCardListener) {
        this.tryingToPlayCardListener = tryingToPlayCardListener;
    }
}