package view.gameElements;

import java.util.HashMap;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

/* --- Mine ------------------------------- */

import view.CUView;

import events.EventListener;
import events.Event;

public class SelectionPane extends HBox implements EventListener {
    /* --- Singleton -------------------------- */

    private static SelectionPane instance;

    public static SelectionPane getInstance() {
        if (instance == null)
            instance = new SelectionPane();
        return instance;
    }

    private SelectionPane() {
        getStyleClass().add("selection-pane");
        setSpacing(20.0);
        setVisible(false);
        setAlignment(Pos.CENTER);
    }

    /* --- Body ------------------------------- */

    public void newSelection(Card[] nodes) {
        getChildren().addAll(nodes);
        setVisible(true);
    }

    public void completeSelection() {
        setVisible(false);
        getChildren().clear();
    }

    /* --- Observer --------------------------- */

    @Override
    public void update(Event event, HashMap<String, Object> data) {
        switch (event) {
            case USER_SELECTING_CARD:
                Platform.runLater(() -> newSelection((Card[]) data.get("all-card-nodes")));
                CUView.communicate(Event.USER_SELECTING_CARD, data);
                break;
            case SELECTION:
                Platform.runLater(() -> completeSelection());
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }
}
