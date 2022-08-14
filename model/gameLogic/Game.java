package model.gameLogic;

import java.util.Arrays;
import java.util.Collection;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import model.data.CardsInfo;

/**
 * This class represents a game frame. Think of this class as a node
 * in a finite state machine. The transitions between nodes are
 * performed by the MainLoop. Any destination node is still represent by this
 * class, but with different values in its fields.
 * <hr>
 * Following this analogy, the Game can not modify itself, but it can only
 * return its values to the requesters. The MainLoop will modify the Game's
 * fileds.
 */
public class Game {
    /* SINGLETON PATTERN */

    private static Game instance;

    public static Game getInstance() {
        if (instance == null)
            instance = new Game();
        return instance;
    }

    private Game() {
        discardPile = new CardGroup();
        restorePlayCondition();
        restoreWinCondition();
    }

    /* FIELDS */

    private TreeMap<Integer, Player> players;
    private Card terrainCard;
    private CardGroup deck = standardDeck();
    private CardGroup discardPile;
    private Player[] turnOrder;
    private int currentTurn;
    private Predicate<Card> playCondition;
    private Predicate<Player> winCondition;
    private boolean isOver;

    int getNextTurn() {
        return (currentTurn + 1) % countPlayers();
    }

    Player getNextPlayer() {
        return turnOrder[getNextTurn()];
    }

    /* GETTERS AND SETTERS */
    void setPlayers(TreeMap<Integer, Player> players) {
        this.players = players;
    }

    Card getTerrainCard() {
        return terrainCard;
    }

    void setTerrainCard(Card terrainCard) {
        this.terrainCard = terrainCard;
    }

    CardGroup getDeck() {
        return deck;
    }

    CardGroup getDiscardPile() {
        return discardPile;
    }

    int getTurn() {
        return currentTurn;
    }

    Player[] getTurnOrder() {
        return turnOrder;
    }

    void setTurnOrder(Player[] newOrder) {
        turnOrder = newOrder;
    }
    
    void setPlayConditon(Predicate<Card> newCondition) {
        playCondition = newCondition;
    }
    
    void setWinCondition(Predicate<Player> newCondition) {
        winCondition = newCondition;
    }
    
    boolean isOver() {
        return isOver;
    }
    
    /* USEFUL METHODS */
    
    Collection<Player> getPlayers() {
        return players.values();
    }

    Player getCurrentPlayer() {
        return turnOrder[currentTurn];
    }

    Player getPlayerByTurn(int theirTurn) {
        return turnOrder[theirTurn % countPlayers()];
    }

    int countPlayers() {
        return players.size();
    }

    int getTurnOf(Player player) {
        return Arrays.asList(turnOrder).indexOf(player);
    }

    void setTurn(Player player) {
        for (int i = 0; i < countPlayers(); i++)
            if (turnOrder[i] == player)
                currentTurn = i;
    }

    boolean isPlayable(Card c) {
        return playCondition.test(c);
    }

    boolean didPlayerWin(Player p) {
        return winCondition.test(p);
    }


    /* RESET */
    /* ----- */
    static void reset() {
        instance = null;
    }

    void end() {
        isOver = true;
    }

    void restorePlayCondition() {
        playCondition = defaultPlayCondition();
    }
    
    void restoreWinCondition() {
        winCondition = defaultWinCondition();
    }
    
    void restoreTurnOrder() {
        turnOrder = defaultTurnOrder();
    }

    /* DEFAULT */
    /* ------- */
    private Player[] defaultTurnOrder() {
        return IntStream.range(0, countPlayers()).mapToObj(i -> players.get(i)).toArray(Player[]::new);
    }

    private Predicate<Player> defaultWinCondition() {
        return player -> {
            return player.hand.isEmpty();
        };
    }

    private Predicate<Card> defaultPlayCondition() {
        return card -> {
            Suit aS = terrainCard.getSuit();
            Suit bS = card.getSuit();
            return aS == Suit.WILD || bS == Suit.WILD || aS == bS || terrainCard.getValue() == card.getValue();
        };
    }

    private CardGroup standardDeck() {
        return CardsInfo.load("Standard");
    }
}