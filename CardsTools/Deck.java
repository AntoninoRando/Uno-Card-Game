package CardsTools;
import java.util.List;

public class Deck extends CardGroup {
    public Deck(Card... cards) {
        super(cards);
    }

    public Deck(List<Card> cards) {
        super(cards);
    }

    public void deal(int index, CardGroup to) {
        to.add(this.remove(index));
        // !Non biosgna fare size-- perche' gia' ci pensa il metodo remove
    }
}
