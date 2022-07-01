package model.cards;

public class Card implements Comparable<Card> {
    // VARIABLES
    private Suit suit;
    private int value;

    // CONSTRUCTORS
    public Card(Suit suit, int value) {
        this.suit = suit;
        this.value = value;
    }

    // METHODS

    // GETTERS AND SETTERS
    public Suit getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    // CONVERTERS
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(suit).append(" ").append(value);
        return sb.toString();
    }

    // COMPARABLE METHODS
    @Override // Used to order card.
    public int compareTo(Card o) {
        // !Non so se sia giusto usare == per gli enum
        if (suit != o.suit) 
            return suit.compareTo(o.suit);
        return Integer.compare(value, o.value);
    }
}