package model;

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
    /* IMPLEMENTING SINGLETON PATTERN */
    /* ------------------------------ */
    private static Game instance;

    public static Game getInstance() {
        if (instance == null)
            instance = new Game();
        return instance;
    }

    private Game() {
        discardPile = new CardGroup();
        defaultPlayCondition = (card) -> {
            Suit aS = terrainCard.getSuit();
            Suit bS = card.getSuit();
            return aS == Suit.WILD || bS == Suit.WILD ? true : aS == bS || terrainCard.getValue() == card.getValue();
        };
        playCondition = defaultPlayCondition;
    }

    static void reset() {
        instance = null;
    }

    /* FIELDS */
    /* ------ */
    TreeMap<Integer, Player> players;
    int turn;

    Deck deck;
    Card terrainCard;
    CardGroup discardPile;

    Predicate<Card> playCondition;
    Predicate<Card> defaultPlayCondition;

    boolean isOver;

    /* GETTERS AND SETTERS */
    /* ------------------- */
    public void setPlayers(TreeMap<Integer, Player> players) {
        this.players = players;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public Player getPlayer() {
        return players.get(turn);
    }

    public Player getPlayer(int theirTurn) {
        return players.get(theirTurn % countPlayers());
    }

    public int countPlayers() {
        return players.size();
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int i) {
        turn = i % countPlayers();
    }

    public int getTurn(Player p) {
        return players.entrySet().stream()
                .filter(entry -> p.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst().get();
    }

    public void nextTurn() {
        if (++turn >= countPlayers())
            turn = 0;
    }

    public boolean isOver() {
        return isOver;
    }

    public void setPlayConditon(Predicate<Card> newCondition) {
        playCondition = newCondition;
    }

    public void setPlayConditonToDefault() {
        playCondition = defaultPlayCondition;
    }

    public boolean isPlayable(Card c) {
        return playCondition.test(c);
    }

    public void end() {
        isOver = true;
    }
}