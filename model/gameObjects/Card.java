package model.gameObjects;

import java.util.HashMap;
import java.util.Optional;

/* --- Mine ------------------------------- */

import model.gameLogic.Effect;

public class Card implements Comparable<Card> {
    /* --- Fields ----------------------------- */

    private Suit suit;
    private int value;
    private Optional<Effect> effect;
    private int tag;
    private static int tagCounter = 24;

    /* ---.--- Getters and Setters ------------ */

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Optional<Effect> getEffect() {
        return effect;
    }

    public void setEffect(Optional<Effect> e) {
        effect = e;
    }

    public int getTag() {
        return tag;
    }

    /* --- Constructors ----------------------- */

    public Card(Suit suit, int value) {
        this.suit = suit;
        this.value = value;
        effect = Optional.empty();
        this.tag = ++Card.tagCounter;
    }

    public Card(Suit suit, int value, Effect effect) {
        this.suit = suit;
        this.value = value;
        setEffect(Optional.of(effect));
        this.tag = ++Card.tagCounter;
    }

    /* --- Body ------------------------------- */

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(suit).append(" ").append(value);
        return sb.toString();
    }

    public Card getCopy() {
        return effect.isPresent() ? new Card(suit, value, effect.get()) : new Card(suit, value);
    }

    public HashMap<String, Object> getData() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("card-tag", getTag());
        data.put("card-representation", toString());
        return data;
    }

    /* --- Comparable ------------------------- */

    @Override
    public int compareTo(Card o) {
        return suit != o.suit ? suit.compareTo(o.suit) : Integer.compare(value, o.value);
    }
}