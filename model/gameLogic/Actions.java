package model.gameLogic;

/* --- Mine ------------------------------- */

import events.toView.EventType;
import prefabs.Card;
import prefabs.Player;

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
    static void changeCurrentCard(Card card) {
        Game game = Game.getInstance();
        game.getDiscardPile().add(game.getTerrainCard());
        game.setTerrainCard(card);
    }

    /**
     * 
     * @return The first card in the deck pile.
     */
    static Card takeFromDeck() {
        Game game = Game.getInstance();
        if (game.getDeck().isEmpty())
            shuffleDeck();
        return game.getDeck().remove(0);
    }

    /**
     * Shuffles the deck.
     */
    static void shuffleDeck() {
        Game game = Game.getInstance();
        game.getDeck().addAll(game.getDiscardPile());
        game.getDeck().shuffle();
        game.getDiscardPile().clear();
    }

    /**
     * Gives the first caard in the deck pile.
     * 
     * @param player The player that will receive the card.
     */
    static void dealFromDeck(Player player) {
        Card card = takeFromDeck();
        player.getHand().add(card);
        if (player.isHuman())
            Loop.events.notify(EventType.USER_DREW, card);
        else {
            Loop.events.notify(EventType.PLAYER_DREW, player);
            Loop.events.notify(EventType.PLAYER_DREW, card);
        }
        Loop.events.notify(EventType.PLAYER_HAND_INCREASE, player);
    }

    /**
     * Gives cards from deck.
     * 
     * @param player The player that will receive the cards.
     * @param times  The amount of cards to give.
     */
    static void dealFromDeck(Player player, int times) {
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
        Loop.getInstance().jumpToPhase(Loop.getInstance().getPhasesQuantity() - 2);
        Loop.events.notify(EventType.TURN_BLOCKED, Game.getInstance().getCurrentPlayer());
    }

    /**
     * Transforms a card into another.
     * 
     * @param source The card to transfrom.
     * @param target The card to become.
     */
    static void transformCard(Card source, Card target) {
        source.setSuit(target.getSuit());
        source.setValue(target.getValue());
        source.setEffect(target.getEffect());
        source.getGuiContainer().update(source);
    }
}
