package CardsTools;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CardGroup {
    // VARIABLES
    protected int size;
    protected List<Card> cards = new LinkedList<Card>();

    // CONSTRUCTORS
    public CardGroup(Card... cards) {
        size = cards.length;
        Collections.addAll(this.cards, cards);
    }

    public CardGroup(Collection<Card> cards) {
        size = cards.size();
        this.cards.addAll(cards);
    }

    // METHODS
    public boolean add(Card card) {
        size++;
        return cards.add(card);
    }

    public boolean addAll(Card... cards) {
        size += cards.length;
        return Collections.addAll(this.cards, cards);
    }

    public boolean addAll(Collection<Card> cards) {
        size += cards.size();
        return this.cards.addAll(cards);
    }

    public Card remove(int index) {
        size--;
        return cards.remove(index);
    }

    public void clear() {
        size = 0;
        cards.clear();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public CardGroup shuffle() {
        Collections.shuffle(cards);
        return this;
    }

    public void forEach(Consumer<Card> action) {
        cards.forEach(action);
    }

    // !Non so come altro farlo
    public void forEach(BiConsumer<Integer, Card> action) {
        for (int i = 0; i < size; i++) {
            Card card = cards.get(i);
            action.accept(i, card);
        }
    }

    // GETTERS AND SETTERS
    public int size() {
        return size;
    }

    public Card getCard(int index) {
        return cards.get(index);
    }

    public Collection<Card> getCards() {
        return cards;
    }

    // CONVERTERS
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("[ ");
        this.forEach((i, card) -> sb.append("(").append(i + 1).append(")").append(card).append(" "));
        sb.append("]");

        return sb.toString();
    }
}