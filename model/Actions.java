package model;

import model.cards.Card;
import model.cards.Suit;
import model.listeners.TerrainListener;
import view.ConsoleOutput;

/**
 * This class contains several static methods to modify the game state. If an
 * action should be displayed, a listener will be triggered. Complex actions can
 * be performed cascading multiple actions.
 */
public abstract class Actions {
    /* LISTENERS */
    /* --------- */
    private static TerrainListener terrainListener = ConsoleOutput.getInstance();

    /* PRIVATE METHODS */
    /* --------------- */
    public static boolean isPlayable(Card a, Card b) {
        Suit aS = a.getSuit();
        Suit bS = b.getSuit();
        return aS == Suit.WILD || bS == Suit.WILD ? true : aS == bS || a.getValue() == b.getValue();
    }

    /* ACTIONS */
    /* ------- */
    public static void changeCurrentCard(Card c) {
        Game game = Game.getInstance();
        game.discardPile.add(game.terrainCard);
        game.terrainCard = c;
        terrainListener.cardChanged(c);
    }

    public static boolean tryChangeCard(Card c) {
        Game game = Game.getInstance();
        if (!isPlayable(game.terrainCard, c))
            return false;
        changeCurrentCard(c);
        return true;
    }

    public static Card takeFromDeck() {
        Game game = Game.getInstance();
        if (game.deck.isEmpty())
            shuffle();
        return game.deck.remove(0);
    }

    public static void dealFromDeck(int i) {
        Game game = Game.getInstance();
        Card c = takeFromDeck();
        game.getPlayer(i).addCard(c);
    }

    public static void dealFromDeck(int i, int times) {
        while (times-- > 0)
            dealFromDeck(i);
    }

    public static void discardCard(Card c) {
        Game game = Game.getInstance();
        game.discardPile.add(0, c);
    }

    public static void changeTurnsOrder(Game game, int... newOrder) {
        // TO-DO!
    }

    public static void blockTurn(Game game, int i) {
        // TO-DO!
        // int playerNumber = game.turnsOrder[i];

        // // !Rende il numero del turno negativo. I numeri negativi, quando incontrati,
        // // vengono resi di nuovo positivi ma vengono trascurati.
        // // !Alternativamente stavo pensando di modificare e farlo con la priorità:
        // chi
        // // ha il numero più alto inizia, ecc...
        // game.turnsOrder[i] = Integer.min(playerNumber, -playerNumber);
    }

    public static void shuffle() {
        Game game = Game.getInstance();
        game.deck.addAll(game.discardPile);
        game.deck.shuffle();
        game.discardPile.clear();
    }
}
