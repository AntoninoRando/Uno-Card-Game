package model.gameLogic;

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
        game.getDiscardPile().add(game.getTerrainCard());
        game.setTerrainCard(c);
    }

    public static Card takeFromDeck() {
        Game game = Game.getInstance();
        if (game.getDeck().isEmpty())
            shuffleDeck();
        return game.getDeck().remove(0);
    }

    public static void shuffleDeck() {
        Game game = Game.getInstance();
        game.getDeck().addAll(game.getDiscardPile());
        game.getDeck().shuffle();
        game.getDiscardPile().clear();
    }

    public static void dealFromDeck(Player player) {
        Card card = takeFromDeck();
        player.getHand().add(card);
        Loop.events.notify("playerDrew", player, card);
    }

    public static void dealFromDeck(Player p, int times) {
        while (times-- > 0)
            dealFromDeck(p);
    }

    public static void discardCard(Card c) {
        Game game = Game.getInstance();
        game.getDiscardPile().add(0, c);
    }

    public static void skipTurn() {
        Loop.currentPhase = Loop.getInstance().phases.size() - 2;
        Loop.events.notify("turnBlocked", Game.getInstance().getPlayer());
    }

    public static void transformCard(Card source, Card target) {
        source.setSuit(target.getSuit());
        source.setValue(target.getValue());
        source.setEffect(target.getEffect());
        source.getGuiContainer().update(source);
    }
}
