package model.gameLogic;

import java.util.Optional;

import view.gameElements.CardContainer;

public class Card implements Comparable<Card> {
    private Suit suit;
    private int value;
    private Optional<Effect> effect;
    private int ID;

    public Card(Suit suit, int value) {
        this.suit = suit;
        this.value = value;
        effect = Optional.empty();
    }

    public Card(Suit suit, int value, Effect effect) {
        this.suit = suit;
        this.value = value;
        setEffect(Optional.of(effect));
        ID = generateID();
    }

    /* GETTERS AND SETTERS */
    /* ------------------- */
    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
        ID = generateID();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        ID = generateID();
    }

    public Optional<Effect> getEffect() {
        return effect;
    }

    public void setEffect(Optional<Effect> e) {
        effect = e;
        ID = generateID();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(suit).append(" ").append(value);
        return sb.toString();
    }

    public int getID() {
        return ID;
    }

    private int generateID() {
        // ID: xxxx xxxx xxxx - 1st field = suit; 2nd field = value; 3rd field = effect
        int f1 = suit.ordinal() * (int) Math.pow(10, 8);
        int f2 = value * (int) Math.pow(10, 4);
        int f3 = 0; // TODO
        return f1 + f2 + f3;
    }

    public static Card getCardFromID(int ID) {
        Suit suit = Suit.values()[ID / (int) Math.pow(10, 8)];
        int value = ID / (int) Math.pow(10, 4);
        return new Card(suit, value);
    }

    public Card getCopy() {
        return effect.isPresent() ? new Card(suit, value, effect.get()) : new Card(suit, value);
    }

    @Override
    public int compareTo(Card o) {
        return suit != o.suit ? suit.compareTo(o.suit) : Integer.compare(value, o.value);
    }

    /* ------------------------- */
    private CardContainer guiContainer;

    public CardContainer getGuiContainer() {
        // We inizialize this filed only when needed, since not every card must be seen
        // in the gui.
        if (guiContainer == null)
            guiContainer = new CardContainer(this);
        return guiContainer;
        // TODO fare che si distrugge il guiContiner quando non si vede la carta nella
        // gui
    }
}