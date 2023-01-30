package model.gameLogic;

import java.util.Collections;
import java.util.HashMap;

/* --- Mine ------------------------------- */

import events.Event;

import model.CUModel;
import model.cards.Card;
import model.players.GameAI;
import model.players.Player;

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
        if (Game.getTerrainCard() != null)
            Game.getDiscardPile().add(Game.getTerrainCard());
        Game.setTerrainCard(card);
    }

    /**
     * 
     * @return The first card in the deck pile.
     */
    public static Card takeFromDeck() {
        if (Game.getDeck().isEmpty())
            shuffleDeck();
        return Game.getDeck().remove(0);
    }

    /**
     * Shuffles the deck.
     */
    public static void shuffleDeck() {
        Game.getDiscardPile().forEach(card -> card.shuffleIn(Game.getDeck()));
        Collections.shuffle(Game.getDeck());
        Game.getDiscardPile().clear();
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
                CUModel.communicate(Event.AI_DREW, data);
        }
    }

    /**
     * Jumps to the turn ahead by the given amount.
     * 
     * @param ahead The amount of turns to skip.
     */
    public static void advanceTurn(int ahead) {
        int newTurn = (Game.getTurn() + ahead) % Game.countPlayers();
        Game.setTurn(newTurn);
    }

    public static void nextPlayer() {

    }

}