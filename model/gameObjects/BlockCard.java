package model.gameObjects;

import java.util.Collection;

import events.Event;
import model.CUModel;
import model.gameEntities.Player;
import model.gameLogic.Game;

public class BlockCard extends Card{
    public BlockCard(Suit suit, int value) {
        super(suit, value);
    }

    @Override
    public void play() {
        Game game = Game.getInstance();
        Player blocked = game.getNextPlayer();
        game.advanceTurn(1);
        CUModel.communicate(Event.TURN_BLOCKED, blocked.getData());
    }

    @Override
    public void shuffleIn(Collection<Card> cards) {
        cards.add(this);
    } 
}
