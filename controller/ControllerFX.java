package controller;

import model.cards.Card;
import view.DeckContainer;

public class ControllerFX extends Controller {
    public void dragControlOnCard(Card card) {
        new ControlDrag(card, this);
    }

    @Override
    public void setupControls() {
        for (Card card : source.getHand())
            dragControlOnCard(card);
        new ControlDraw(DeckContainer.getInstance(), this);
    }

    @Override
    public void update(String eventType, Object data) {
        if (eventType.equals("add"))
            dragControlOnCard((Card) data); // TODO dara errore se viene chiamato addAll che passa come data una collection
    }
}
