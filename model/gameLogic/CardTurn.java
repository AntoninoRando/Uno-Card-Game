package model.gameLogic;

import model.cards.Card;

public class CardTurn implements GameState {
    /* --- Fields ----------------------------- */

    private Card card;
    private Game game;

    /* --- State ------------------------------ */

    public void setContext(Card card, Game game) {
        this.card = card;
        this.game = game;
    }

    @Override
    public void resolve() {
        card.play(game);
        TransitionState nextState = new TransitionState();
        nextState.setContext(game);
        game.changeState(nextState);
    }
}
