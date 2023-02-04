package view.gameElements;

import java.util.Map;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

/* --- Mine ------------------------------- */

import view.CUView;

import events.EventListener;

public class SelectionPane extends HBox implements EventListener {
    /* --- Singleton -------------------------- */

    private static SelectionPane instance;

    public static SelectionPane getInstance() {
        if (instance == null)
            instance = new SelectionPane();
        return instance;
    }

    private SelectionPane() {
        CUView.getInstance().subscribe(this, "USER_SELECTING_CARD", "SELECTION", "GAME_READY");
        
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
    public void update(String event, Map<String, Object> data) {
        switch (event) {
            case "USER_SELECTING_CARD":
                Platform.runLater(() -> newSelection((Card[]) data.get("all-card-nodes")));
                CUView.communicate("USER_SELECTING_CARD", data);
                break;
            case "GAME_READY":
            case "SELECTION":
                Platform.runLater(() -> completeSelection());
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }
}
