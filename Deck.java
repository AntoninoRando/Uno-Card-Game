import java.util.List;
import java.util.Collections;

public class Deck {
    private int size;
    private List<Card> cards;

    public Deck(Card... cards) {
        this.cards = List.of(cards);
        size = cards.length;
    }

    public int getSize() {
        return size;
    }

    public void shuffleDeck() {
        Collections.shuffle(cards);
    }

    public Card deal(int index) {
        size--;
        return cards.remove(index);
    }
}
