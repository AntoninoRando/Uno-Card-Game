package model;

import model.cards.Card;
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
        return a.getSuit() == b.getSuit() || a.getValue() == b.getValue();
    }

    /* ACTIONS */
    /* ------- */
    public static void changeCurrentCard(Game game, Card c) {
        game.discardPile.add(game.terrainCard);
        game.terrainCard = c;
        terrainListener.cardChanged(c);
    }

    public static boolean tryChangeCard(Game game, Card c) {
        if (!isPlayable(game.terrainCard, c))
            return false;
        changeCurrentCard(game, c);
        return true;
    }

    public static Card takeFromDeck(Game game) {
        if (game.deck.isEmpty())
            shuffle(game);
        return game.deck.remove(0);
    }

    public static void dealFromDeck(Game game, int i) {
        Card c = takeFromDeck(game);
        game.players.get(i).addCard(c);
    }

    public static void dealFromDeck(Game game, int i, int times) {
        while (times-- > 0)
            dealFromDeck(game, i);
    }

    public static void discardCard(Game game, Card c) {
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

    public static void shuffle(Game game) {
        game.deck.addAll(game.discardPile);
        game.deck.shuffle();
        game.discardPile.clear();
    }
}
