package model.gameLogic;

import events.EventType;

import prefabs.Card;
import prefabs.Player;

/**
 * A class containing static methods that modify the current game state.
 */
public abstract class Actions {
    static void changeCurrentCard(Card newCard) {
        Game game = Game.getInstance();
        game.getDiscardPile().add(game.getTerrainCard());
        game.setTerrainCard(newCard);
    }

    static Card takeFromDeck() {
        Game game = Game.getInstance();
        if (game.getDeck().isEmpty())
            shuffleDeck();
        return game.getDeck().remove(0);
    }

    static void shuffleDeck() {
        Game game = Game.getInstance();
        game.getDeck().addAll(game.getDiscardPile());
        game.getDeck().shuffle();
        game.getDiscardPile().clear();
    }

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

    static void dealFromDeck(Player p, int times) {
        while (times-- > 0)
            dealFromDeck(p);
    }

    static void discardCard(Card card) {
        Game.getInstance().getDiscardPile().add(0, card);
    }

    static void skipTurn() {
        Loop.getInstance().jumpToPhase(Loop.getInstance().getPhasesQuantity() - 2);
        Loop.events.notify(EventType.TURN_BLOCKED, Game.getInstance().getCurrentPlayer());
    }

    static void transformCard(Card source, Card target) {
        source.setSuit(target.getSuit());
        source.setValue(target.getValue());
        source.setEffect(target.getEffect());
        source.getGuiContainer().update(source);
    }
}
