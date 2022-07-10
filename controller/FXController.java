package controller;

import model.cards.Card;

public class FXController extends Controller {
    @Override
    public void setupPlayer() {
        for (Card card : source.getHand())
            new DragControl(card, this);
    }
}
