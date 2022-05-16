import java.util.Collections;

public class Deck extends CardGroup {
    public void shuffle() {
        Collections.shuffle(cards);
    }

    public void deal(int index, CardGroup to) {
        size--;
        to.add(cards.remove(index));
    }
}
