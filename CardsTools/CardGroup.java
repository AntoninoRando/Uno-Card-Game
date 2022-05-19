package CardsTools;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CardGroup implements Collection<Card> {
    // VARIABLES
    protected List<Card> cards = new LinkedList<Card>();

    // CONSTRUCTORS
    public CardGroup(Card... cards) {
        Collections.addAll(this.cards, cards);
    }

    public CardGroup(Collection<Card> cards) {
        this.cards.addAll(cards);
    }

    // METHODS
    public void shuffle() {
        Collections.shuffle(cards);
    }

    public void arrange() {
        cards.sort(Card::compareTo);
    }

    // Overload that support a BiConsumer: index, card
    // !Non so come altro farlo
    public void forEach(BiConsumer<Integer, Card> action) {
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            action.accept(i, card);
        }
    }

    // GETTERS AND SETTERS
    public Card get(int index) {
        return cards.get(index);
    }
    
    public Collection<Card> getAll() {
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
    
    // COLLECTION METHODS
    @Override
    public boolean contains(Object o) {
        return cards.contains(o);
    }
    
    @Override
    public Iterator<Card> iterator() {
        return cards.iterator();
    }
    
    @Override
    public void forEach(Consumer<? super Card> action) {
        cards.forEach(action);
    }

    @Override
    public Object[] toArray() {
        return cards.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return cards.toArray(a);
    }

    @Override
    public boolean remove(Object o) {
        return cards.remove(o);
    }
    
    public Card remove(int i) {
        return cards.remove(i);
    }
    
    @Override
    public boolean containsAll(Collection<?> c) {
        return cards.containsAll(c);
    }
    
    @Override
    public boolean add(Card e) {
        return cards.add(e);
    }
    
    @Override
    public boolean addAll(Collection<? extends Card> c) {
        return cards.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return cards.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return cards.retainAll(c);
    }
    
    @Override
    public int size() {
        return cards.size();
    }

    @Override
    public boolean isEmpty() {
        return cards.isEmpty();
    }
    
    @Override
    public void clear() {
        cards.clear();
    }
}