package model.cards;

import java.util.Optional;

import model.effects.Effect;

public class Card implements Comparable<Card> {
    private Suit suit;
    private int value;
    private int ID;

    private Optional<Effect> effect;

    public Card(Suit suit, int value) {
        this.suit = suit;
        this.value = value;

        effect = Optional.empty();
    }

    /* GETTERS AND SETTERS */
    /* ------------------- */
    public Suit getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    public int getID() {
        return ID;
    }

    public Optional<Effect> getEffect() {
        return effect;
    }

    public void addEffect(Effect e) {
        effect = Optional.of(e);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(suit).append(" ").append(value);
        return sb.toString();
    }
    @Override
    public int compareTo(Card o) {
        return suit != o.suit ? suit.compareTo(o.suit) : Integer.compare(value, o.value);
    }
}