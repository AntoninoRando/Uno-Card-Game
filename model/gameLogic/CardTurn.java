package model.gameLogic;

/* --- JUno ------------------------------- */

import model.cards.Card;

/**
 * The game state in which the card will activate its effect. After that, there
 * will be the TransitionState.
 */

public class CardTurn implements GameState {
    /* --- State ------------------------------ */

    /**
     * The context.
     */
    private Card card;
    /**
     * The context.
     */
    private Game game;

    /**
     * Sets the context to execute this state properly.
     * 
     * @param card The card that has been played and thus should activate its
     *             effects.
     * @param game The current game in which the card has been played.
     */
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
