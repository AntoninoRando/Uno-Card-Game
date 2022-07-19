package model;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;

import model.cards.Card;
import model.cards.CardGroup;
import model.cards.Deck;
import model.cards.Suit;

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
    /* ----------------- */
    private static Game instance;

    public static Game getInstance() {
        if (instance == null)
            instance = new Game();
        return instance;
    }

    private Game() {
        discardPile = new CardGroup();
        playCondition = defaultPlayCondition;
        winCondition = defaultWinCondition;
    }

    static void reset() {
        instance = null;
    }

    /* FIELDS */
    /* ------ */
    private TreeMap<Integer, Player> players;
    private int turn;

    protected Deck deck;
    protected Card terrainCard;
    protected CardGroup discardPile;

    private Predicate<Card> playCondition;
    private final Predicate<Card> defaultPlayCondition = card -> {
        Suit aS = terrainCard.getSuit();
        Suit bS = card.getSuit();
        return aS == Suit.WILD || bS == Suit.WILD || aS == bS || terrainCard.getValue() == card.getValue();
    };

    private Predicate<Player> winCondition;
    private final Predicate<Player> defaultWinCondition = player -> {
        return player.hand.isEmpty();
    };

    private boolean isOver;

    /* GETTERS AND SETTERS */
    /* ------------------- */
    public Player getPlayer() {
        return players.get(turn);
    }

    public Player getPlayer(int theirTurn) {
        return players.get(theirTurn % countPlayers());
    }

    public int countPlayers() {
        return players.size();
    }

    public Collection<Player> players() {
        return players.values();
    }

    public void setPlayers(TreeMap<Integer, Player> players) {
        this.players = players;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }


    public int getTurn() {
        return turn;
    }

    public void setTurn(int i) {
        turn = i % countPlayers();
    }

    public void setTurn(Player p) {
        turn = getTurn(p);
    }

    public int getTurn(Player p) {
        return players.entrySet().stream()
                .filter(entry -> p.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst().get();
    }

    public boolean isOver() {
        return isOver;
    }

    public boolean isPlayable(Card c) {
        return playCondition.test(c);
    }

    public void setPlayConditon(Predicate<Card> newCondition) {
        playCondition = newCondition;
    }

    public void restorePlayCondition() {
        playCondition = defaultPlayCondition;
    }

    public boolean didPlayerWin(Player p) {
        return winCondition.test(p);
    }

    public void restoreWinCondition() {
        winCondition = defaultWinCondition;
    }

    public void end() {
        isOver = true;
    }
}