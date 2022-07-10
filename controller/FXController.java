package controller;

import model.cards.Card;
import view.DeckContainer;

public class FXController extends Controller {
    @Override
    public void setupPlayer() {
        for (Card card : source.getHand())
            new DragControl(card, this);
        new DrawControl(DeckContainer.getInstance(), this);
    }
}
