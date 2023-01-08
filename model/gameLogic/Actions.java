package model.gameLogic;

import java.util.HashMap;

import events.Event;
import model.CUModel;
import model.gameEntities.GameAI;
import model.gameEntities.Player;
import model.gameObjects.*;

/**
 * A class containing static methods that modify the current game state.
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
        for (Card card : game.getDiscardPile())
            card.shuffleIn(game.getDeck());
        game.getDeck().shuffle();
        game.getDiscardPile().clear();
    }

    /**
     * Gives the first caard in the deck pile.
     * 
     * @param player The player that will receive the card.
     */
    public static void dealFromDeck(Player player) {
        // Add card
        Card card = takeFromDeck();
        player.getHand().add(card);
        HashMap<String, Object> data = card.getData();
        data.putAll(player.getData());
        // Notify
        if (!(player instanceof GameAI))
            CUModel.communicate(Event.USER_DREW, data);
        else {
            CUModel.communicate(Event.PLAYER_DREW, data);
        }

        CUModel.communicate(Event.PLAYER_HAND_INCREASE, data);
    }

    /**
     * Gives cards from deck.
     * 
     * @param player The player that will receive the cards.
     * @param times  The amount of cards to give.
     */
    public static void dealFromDeck(Player player, int times) {
        while (times-- > 0)
            dealFromDeck(player);
    }

    /**
     * Throws a card into the discard pile.
     * 
     * @param card The card to throw in the discard pile.
     */
    static void discardCard(Card card) {
        Game.getInstance().getDiscardPile().add(0, card);
    }

    /**
     * Jumps the current turn.
     */
    static void skipTurn() {
        // // Jump phase
        // Loop.getInstance().jumpToPhase(Loop.getInstance().getPhasesQuantity() - 2);
        // // Notify
        // Player player = Game.getInstance().getCurrentPlayer();
        // HashMap<String, Object> data = new HashMap<>();
        // data.put("nickname", player.getNickame());
        // data.put("icon", player.getIcon());
        // Loop.events.notify(EventType.TURN_BLOCKED, data);
    }

    /**
     * Transforms a card into another.
     * 
     * @param source The card to transfrom.
     * @param target The card to become.
     */
    static void transformCard(Card source, Card target) {
        // source.setSuit(target.getSuit());
        // source.setValue(target.getValue());
        // source.setEffect(target.getEffect());
    }
}