package CardsTools;

import EffectsTools.Effect;
import EffectsTools.Selectable;

public class Card implements Selectable<Card>, Comparable<Card> {
    // VARIABLES
    private Suit suit;
    private int value;

    private Effect effect = new Effect();

    // CONSTRUCTORS
    public Card(Suit suit, int value, Effect effect) {
        this.suit = suit;
        this.value = value;
        this.effect = effect;
    }

    public Card(Suit suit, int value) {
        this.suit = suit;
        this.value = value;
    }

    // METHODS
    // !In realtà non aggiunge un effetto, ma lo setta proprio, cancellando il precedente...
    public void addEffect(Effect effect) {
        this.effect = effect;
    }

    // GETTERS AND SETTERS
    public Suit getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    public Effect getEffect() {
        return effect;
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