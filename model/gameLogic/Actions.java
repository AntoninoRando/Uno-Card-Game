package model.gameLogic;

import java.util.HashMap;

/* --- Mine ------------------------------- */

import events.Event;

import model.CUModel;
import model.gameEntities.GameAI;
import model.gameEntities.Player;
import model.gameObjects.Card;

/**
 * A group of actions which manipulate the game.
 */
public abstract class Actions {
    /* --- Body ------------------------------- */

    /**
     * Replace the current terrain card with the given card.
     * 
     * @param card The new terrain card.
     */
    public static void changeCurrentCard(Card card) {
        Game game = Game.getInstance();
        if (game.getTerrainCard() != null)
            game.getDiscardPile().add(game.getTerrainCard());
        game.setTerrainCard(card);
    }

    /**
     * 
     * @return The first card in the deck pile.
     */
    public static Card takeFromDeck() {
        Game game = Game.getInstance();
        if (game.getDeck().isEmpty())
            shuffleDeck();
        return game.getDeck().remove(0);
    }

    /**
     * Shuffles the deck.
     */
    public static void shuffleDeck() {
        Game game = Game.getInstance();
        // TODO potrebbe dare un problema perché modifica la lista su cui itera
        game.getDiscardPile().forEach(card -> card.shuffleIn(game.getDeck()));
        game.getDeck().shuffle();
        game.getDiscardPile().clear();
    }

    /**
     * A player draws from deck the choosen amount of cards.
     * 
     * @param player The player that will draw the cards.
     * @param times  The amount of cards to be drawn.
     */
    public static void dealFromDeck(Player player, int times) {
        while (times-- > 0) {
            // Add card
            Card card = takeFromDeck();
            player.getHand().add(card);

            // Notify
            HashMap<String, Object> data = card.getData();
            data.putAll(player.getData());

            if (!(player instanceof GameAI))
                CUModel.communicate(Event.USER_DREW, data);
            else
                CUModel.communicate(Event.PLAYER_DREW, data);

            CUModel.communicate(Event.PLAYER_HAND_INCREASE, data);
        }
    }

}