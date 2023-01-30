package model.gameLogic;

import model.cards.Card;

public class CardTurn implements GameState {
    /* --- Fields ----------------------------- */

    private Card card;

    /* --- Constructors ----------------------- */

    public CardTurn(Card card) {
        setContext(card);
    }

    /* --- State ------------------------------ */

    public void setContext(Card card) {
        this.card = card;
    }

    @Override
    public void resolve() {
        card.play();
        Game.changeState(TransitionState.getInstance());
    }
}
