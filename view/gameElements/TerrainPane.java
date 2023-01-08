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

    /* --- Fields ----------------------------- */

    private Card terrainCard;

    /* --- Body ------------------------------- */

    private void initialize() {
        terrainCard = new Card();
        getChildren().setAll(terrainCard);
    }

    private void updateTerrainCard(Card node) {
        terrainCard.update(node);
    }

    /* --- Observer --------------------------- */

    @Override
    public void update(Event event, HashMap<String, Object> data) {
        switch (event) {
            case GAME_READY:
                Platform.runLater(() -> initialize());
                break;
            case CARD_CHANGE:
                Platform.runLater(() -> updateTerrainCard((Card) data.get("card-node")));
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }
}