package view.gameElements;

import javafx.application.Platform;
import javafx.scene.layout.StackPane;

import java.util.HashMap;

import events.EventListener;
import events.Event;

/**
 * A GUI element representing the current card on the ground.
 */
public class TerrainPane extends StackPane implements EventListener {
    /* --- Singleton -------------------------- */

    private static TerrainPane instance;

    public static TerrainPane getInstance() {
        if (instance == null)
            instance = new TerrainPane();
        return instance;
    }

    private TerrainPane() {
        getStyleClass().add("terrain");
        setMaxHeight(400);
        setMaxWidth(400);
    }
    
    /* --- Observer --------------------------- */

    @Override
    public void update(Event event, HashMap<String, Object> data) {
        switch (event) {
            case CARD_CHANGE:
                Platform.runLater(() -> getChildren().setAll((Card) data.get("card-node")));
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }
}