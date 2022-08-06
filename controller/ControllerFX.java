package controller;

import model.gameLogic.Card;

import view.gameElements.PlayzonePane;

public class ControllerFX extends Controller {
    @Override
    public void setupControls() {
        for (Card card : source.getHand()) {
            Control control = new ControlDrag(card, PlayzonePane.getInstance());
            control.setHandler(this);
        }
        Controls.DECLARE_UNO.setHandler(this);
        Controls.DRAW.setHandler(this);
    }

    @Override
    public void update(String eventType, Object data) {
        if (eventType.equals("add")) {
            // TODO dara errore se viene chiamato addAll che passa come data una collection
            Control control = new ControlDrag((Card) data, PlayzonePane.getInstance());
            control.setHandler(this);
        }
    }

    @Override
    public void update(String eventType, Object... data) {
        // TODO Auto-generated method stub

    }
}
