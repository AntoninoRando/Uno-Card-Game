public class Card {
    private Suit suit;
    private int value;

    public Card(Suit suit, int value) {
        this.suit = suit;
        this.value = value;
    }

    public boolean isPlayable() {
        // !Non so se sia giusto cosi' perche' sarebbe come fare ==
        return suit.equals(GameManager.getTerrainCard().getSuit());
    }

    public Suit getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        // !Andrebbe fatto con StringBuilder
        return suit + " " + value;
    }
}

enum Suit {
    RED, BLUE, GREEN, YELLOW, BLACK;
}