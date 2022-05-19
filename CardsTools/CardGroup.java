package CardsTools;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;

// !Dovrebbe implementare Collection così che è persino iterable
public class CardGroup {
    // VARIABLES
    protected int size;
    // !Non sono sicuro vada usato ArrayList
    protected ArrayList<Card> cards = new ArrayList<Card>();

    // CONSTRUCTORS
    public CardGroup(Card... cards) {
        size = cards.length;
        Collections.addAll(this.cards, cards);
    }

    public CardGroup(Collection<Card> cards) {
        size = cards.size();
        this.cards.addAll(cards);
    }

    // METHODS !Siccome molti metodo sono gli stessi delle collection, forse si può
    // implementare Collection<Card> stesso.
    public boolean add(Card card) {
        size++;
        return cards.add(card);
    }

    public boolean addAll(Collection<Card> cards) {
        size = cards.size();
        return this.cards.addAll(cards);
    }

    public void clear() {
        size = 0;
        cards.clear();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public Card remove(int index) {
        size--;
        return cards.remove(index);
    }

    public CardGroup shuffle() {
        Collections.shuffle(cards);
        return this;
    }

    // GETTERS AND SETTERS
    public int getSize() {
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
        for (int i = 0; i < size; i++) {
            sb.append("(").append(i+1).append(")");
            sb.append(cards.get(i));
            sb.append(" ");
        }
        sb.append("]");

        return sb.toString();
    }
}
