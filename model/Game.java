package model;

import java.util.TreeMap;

import model.cards.Card;
import model.cards.CardGroup;
import model.cards.Deck;

/**
 * This class stores represent a game state.
 */
public class Game {
    private TreeMap<Integer, Player> players;
    private int turn;

    protected Deck deck;
    protected Card terrainCard;
    protected CardGroup discardPile;

    private boolean end;

    public Game(TreeMap<Integer, Player> players, Deck deck) {
        this.players = players;
        this.deck = deck;

        turn = 1;
        discardPile = new CardGroup();
    }

    protected void reset() {
        // TO-DO!
    }

    public Player getPlayer() {
        return players.get(turn);
    }

    public Player getPlayer(int theirTurn) {
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