import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;

public class CardGroup {
    protected int size;
    // !Non sono sicuro vada usato ArrayList
    protected ArrayList<Card> cards = new ArrayList<Card>();

    public CardGroup(Card... cards) {
        Collections.addAll(this.cards, cards);
        size = cards.length;
    }

    public CardGroup(Collection<Card> cards) {
        this.cards.addAll(cards);
        size = cards.size();
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

    public Card getCard(int index) {
        return cards.get(index);
    }

    public String toString() {
        return cards.toString();
    }
}
