package model.cards;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import model.events.EventManager;

// !Visto che le carte nel gruppo potrebbero cambiare per via di molti effetti, 
// potrei aggiungere un metodo che permette di iterare anche se viene modificato 
// il cardGroup
public class CardGroup implements Collection<Card> {
    // VARIABLES
    protected List<Card> cards = new LinkedList<Card>();
    public EventManager observers = new EventManager();

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

    public void add(int index, Card card) {
        cards.add(index, card);
        observers.notify("add", card);
    }

    public int indexOf(Card c) {
        return cards.indexOf(c);
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

    public Card getLast() {
        return cards.get(cards.size()-1);
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
        cards.add(e);
        observers.notify("add", e);
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Card> c) {
        if(cards.addAll(c)) {
            observers.notify("add", c);
            return true;
        }
        return false;
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