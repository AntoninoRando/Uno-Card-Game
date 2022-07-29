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
        Loop.getInstance().events.notify("playerDrew", p);
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
        Loop.getInstance().currentPhase = Loop.getInstance().phases.size() - 2;
        Loop.getInstance().events.notify("turnBlocked", Game.getInstance().getPlayer());
    }

    public static void transformCard(Card source, Card target) {
        source.setSuit(target.getSuit());
        source.setValue(target.getValue());
        source.setEffect(target.getEffect());
        source.getGuiContainer().update(source);
    }

    public static void shuffle() {
        Game game = Game.getInstance();
        game.deck.addAll(game.discardPile);
        game.deck.shuffle();
        game.discardPile.clear();
    }
}
