package GameTools;

import CardsTools.Card;
import GUI.CardChangedListener;

public abstract class Controller {
    // !Visbilità default per usarle nel package
    Player bringer;
    GameController game;
    CardChangedListener cardListener;

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

        if (!game.isPlayable(card)) {
            return false;
        }

        game.changeCurrentCard(bringer.removeCard(cardPosition));

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
}