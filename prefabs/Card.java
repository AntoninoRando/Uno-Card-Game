package prefabs;

import java.util.Optional;

import model.gameLogic.Effect;
import view.gameElements.CardContainer;

public class Card implements Comparable<Card> {
    /* --- Fields ----------------------------- */

    private Suit suit;
    private int value;
    private Optional<Effect> effect;
    private CardContainer guiContainer;

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

    public CardContainer getGuiContainer() {
        if (guiContainer == null)
            guiContainer = new CardContainer(this);
        return guiContainer;
    }

    /* --- Constructors ----------------------- */

    public Card(Suit suit, int value) {
        this.suit = suit;
        this.value = value;
        effect = Optional.empty();
    }

    public Card(Suit suit, int value, Effect effect) {
        this.suit = suit;
        this.value = value;
        setEffect(Optional.of(effect));
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

    /* --- Comparable ------------------------- */

    @Override
    public int compareTo(Card o) {
        return suit != o.suit ? suit.compareTo(o.suit) : Integer.compare(value, o.value);
    }
}