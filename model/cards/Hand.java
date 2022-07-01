package model.cards;

import java.util.List;

public class Hand extends CardGroup {
    public Hand(Card... cards) {
        super(cards);
    }

    public Hand(List<Card> cards) {
        super(cards);
    }
}
