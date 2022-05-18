public class Card {
    // VARIABLES
    private Suit suit;
    private int value;

    // CONSTRUCTORS
    public Card(Suit suit, int value) {
        this.suit = suit;
        this.value = value;
    }

    // METHODS
    public boolean isPlayable(Card card) {
        return suit.equals(card.getSuit()) || value == card.getValue();
    }

    public boolean isPlayable(GameManager game) {
        Card terrainCard = game.getTerrainCard();
        // !Non so se sia giusto equals sugni enum perche' sarebbe come fare ==
        return isPlayable(terrainCard);
    }

    // GETTERS AND SETTERS
    public Suit getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    // CONVERTERS
    public String toString() {
        // !Andrebbe fatto con StringBuilder
        return suit + " " + value;
    }
}

enum Suit {
    RED, BLUE, GREEN, YELLOW, WILD;
}