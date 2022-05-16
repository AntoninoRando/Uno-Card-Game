import java.util.List;

public class CardGroup {
    protected int size;
    protected List<Card> cards;

    public CardGroup(Card... cards) {
        this.cards = List.of(cards);
        size = cards.length;
    }

    public boolean add(Card card) {
        size++;
        return cards.add(card);
    }

    public Card remove(int index) {
        size--;
        return cards.remove(index);
    }

    public int getSize() {
        return size;
    }

    public String toString() {
        return cards.toString();
    }
}
