package model.gameObjects;

import java.util.HashMap;

/* --- Mine ------------------------------- */

/**
 * Follows the <em>Strategy Pattern</em>.
 */
public abstract class Card implements Comparable<Card> {
    /* --- Fields ----------------------------- */

    protected Suit suit;
    protected int value;
    protected int tag;
    protected static int tagCounter = 24;

    /* ---.--- Getters and Setters ------------ */

    public Suit getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    public int getTag() {
        return tag;
    }

    /* --- Constructors ----------------------- */

    public Card(Suit suit, int value) {
        this.suit = suit;
        this.value = value;
        this.tag = ++Card.tagCounter;
    }

    /* --- Body ------------------------------- */

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(suit).append(" ").append(value);
        return sb.toString();
    }

    public HashMap<String, Object> getData() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("card-tag", getTag());
        data.put("card-representation", toString());
        return data;
    }

    /* --- Strategy --------------------------- */

    public abstract void play();

    /* --- Comparable ------------------------- */

    @Override
    public int compareTo(Card o) {
        return suit != o.suit ? suit.compareTo(o.suit) : Integer.compare(value, o.value);
    }
}