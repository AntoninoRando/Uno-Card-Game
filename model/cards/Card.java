package model.cards;

import java.util.Collection;
import java.util.HashMap;

/* --- JUno ------------------------------- */

import model.gameLogic.Game;

/**
 * Follows the <em>Strategy Pattern</em>.
 * <p>
 * An abstract card. It has a suit; value; tag; play effect; reshuffle policy.
 */
public abstract class Card {
    /* --- Fields ----------------------------- */

    protected Suit suit;
    protected int value;
    protected Integer tag;
    protected static int tagCounter;

    /* ---.--- Getters and Setters ------------ */

    public Suit getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    /**
     * Gets this card ID. The card ID is not created until it is needed.
     * @return This card ID.
     */
    public int getTag() {
        if (tag == null)
            tag = ++tagCounter;
        return tag;
    }

    /* --- Constructors ----------------------- */

    /**
     * Creates a card with the given suit and value. The tag is assigned
     * automatically.
     * 
     * @param suit  The card suit.
     * @param value The card value.
     */
    public Card(Suit suit, int value) {
        this.suit = suit;
        this.value = value;
    }

    /* --- Body ------------------------------- */

    /**
     * Wraps the card info and returns it.
     * 
     * @return The card data: "card-ID" (int) and "card-representation" (String).
     */
    public HashMap<String, Object> getData() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("card-ID", getTag());
        data.put("card-representation", toString());
        return data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(suit).append(" ").append(value);
        return sb.toString();
    }

    /* --- Strategy --------------------------- */

    /**
     * Executes the card effect.
     * 
     * @param game The current math in which this card has been played.
     * 
     */
    public abstract void play(Game game);

    /**
     * Puts this card in the given collection.
     * <p>
     * When inserting cards into a collection, this method should be used so that it
     * is the card to choose the reshuffle policy.
     * 
     * @param cards The collection of destination, that may contain this card after
     *              this method call.
     */
    public void shuffleIn(Collection<Card> cards) {
        cards.add(this);
    }
}