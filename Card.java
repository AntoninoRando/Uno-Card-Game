public class Card {
    private Suit suit;
    private int value;

    public Suit getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    public boolean isPlayable() {
        // !Non so se sia giusto cosi' perche' sarebbe come fare ==
        return suit.equals(GameManager.getTerrainCard().getSuit());
    }
}

enum Suit {
    RED, BLUE, GREEN, YELLOW, BLACK;
}