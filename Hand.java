import java.util.List;

public class Hand extends CardGroup {
    public Hand(Card... cards) {
        super(cards);
    }

    public Hand(List<Card> cards) {
        super(cards);
    }

    // !Dovrei usare una treeList per tenere ordinate le carte
    /**
     * Arrange the cards first by colors, second by value.
     */
    public void arrange() {
    }
}
