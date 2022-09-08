package controller.controls;

import events.EventType;
import prefabs.Card;
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
    public void update(EventType event, Card data) {
        switch (event) {
            // TODO dara errore se viene chiamato addAll che passa come data una collection
            case ADD:
                Control control = new ControlDrag(data, PlayzonePane.getInstance());
                control.setHandler(this);
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }
}
