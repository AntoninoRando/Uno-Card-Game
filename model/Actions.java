package model;

import model.cards.Card;

/**
 * This class contains several static methods to modify the game state. If an
 * action should be displayed, a listener will be triggered. Complex actions can
 * be performed cascading multiple actions.
 */
public abstract class Actions {
    /* ACTIONS */
    /* ------- */
    public static void changeCurrentCard(Card c) {
        Game game = Game.getInstance();
        game.discardPile.add(game.terrainCard);
        game.terrainCard = c;
    }

    public static boolean tryChangeCard(Card c) {
        Game game = Game.getInstance();
        if (!game.isPlayable(c))
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

    public static void dealFromDeck(Player p) {
        Card c = takeFromDeck();
        p.addCard(c);
    }

    public static void dealFromDeck(Player p, int times) {
        while (times-- > 0)
            dealFromDeck(p);
    }

    public static void discardCard(Card c) {
        Game game = Game.getInstance();
        game.discardPile.add(0, c);
    }

    public static void skipTurn() {
        Game.getInstance().nextTurn();
    }

    public static void changeTurnsOrder(int... newOrder) {
        // TO-DO!
    }

    public static void blockTurn(int i) {
        // TO-DO!
        // int playerNumber = game.turnsOrder[i];

        // // !Rende il numero del turno negativo. I numeri negativi, quando incontrati,
        // // vengono resi di nuovo positivi ma vengono trascurati.
        // // !Alternativamente stavo pensando di modificare e farlo con la priorità:
        // // chi ha il numero più alto inizia, ecc...
        // game.turnsOrder[i] = Integer.min(playerNumber, -playerNumber);
    }

    public static void shuffle() {
        Game game = Game.getInstance();
        game.deck.addAll(game.discardPile);
        game.deck.shuffle();
        game.discardPile.clear();
    }
}
