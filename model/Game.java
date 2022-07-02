package model;

import java.util.TreeMap;

import model.cards.Card;
import model.cards.CardGroup;
import model.cards.Deck;

/**
 * This class stores represent a game state.
 */
public class Game {
    protected TreeMap<Integer, Player> players;
    protected int turn;

    protected Deck deck;
    protected Card terrainCard;
    protected CardGroup discardPile;

    protected boolean end;

    public Game(TreeMap<Integer, Player> players, Deck deck) {
        this.players = players;
        this.deck = deck;

        turn = 1;
        discardPile = new CardGroup();
    }

    protected void reset() {
        // TO-DO!
    }
}