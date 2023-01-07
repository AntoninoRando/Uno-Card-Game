package model.gameLogic;

import events.EventType;
import model.CUModel;
import model.gameEntities.GameAI;
import model.gameEntities.Player;
import model.gameObjects.Card;

public class CardTurn implements GameState {
    /* --- Fields ----------------------------- */

    private Game game;
    private Card card;

    /* --- Constructors ----------------------- */

    public CardTurn(Game game, Card card) {
        setContext(game, card);
    }

    /* --- Body ------------------------------- */

    public void passTurn() {
        CUModel.communicate(EventType.TURN_END, game.getCurrentPlayer().getData());

        Player following = game.getNextPlayer();
        game.advanceTurn(1);

        /*
         * "In the State pattern, the particular states may be aware of each other and initiate transitions from one state to another [...]"
         */
        if (following instanceof GameAI)
            game.changeState(AITurn.getInstance(following.getNickame()));
        else
            game.changeState(UserTurn.getInstance());
    }


    /* --- State ------------------------------ */

    public void setContext(Game game, Card card) {
        this.game = game;
        this.card = card;
    }

    @Override
    public void resolve() {
        card.play();
        passTurn();
    }
    
}
