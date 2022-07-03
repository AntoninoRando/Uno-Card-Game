package model;

import java.util.TreeMap;

import model.cards.Card;
import model.cards.CardGroup;
import model.cards.Deck;

/**
 * This class stores represent a game state.
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
        turn = 1;
        discardPile = new CardGroup();
    }

    private TreeMap<Integer, Player> players;
    private int turn;

    protected Deck deck;
    protected Card terrainCard;
    protected CardGroup discardPile;

    private boolean end;

    public void setPlayers(TreeMap<Integer, Player> players) {
        this.players = players;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    protected void reset() {
        // TO-DO!
    }

    public Player getPlayer() {
        return players.get(turn);
    }

    public Player getPlayer(int theirTurn) {
        // theirTurn = Integer.max(theirTurn % countPlayers(), 1);
        return players.get(theirTurn);
    }

    public int countPlayers() {
        return players.size();
    }

    public int getTurn() {
        return turn;
    }

    public void nextTurn() {
        if (++turn > countPlayers())
            turn = 1;
    }

    public boolean isOver() {
        return end;
    }

    public void end() {
        end = true;
    }
}