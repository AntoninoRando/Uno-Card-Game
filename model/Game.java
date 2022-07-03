package model;

import java.util.Map;
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
        return players.get(theirTurn % countPlayers());
    }

    public int countPlayers() {
        return players.size();
    }

    public int getTurn() {
        return turn;
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
        return end;
    }

    public void end() {
        end = true;
    }
}