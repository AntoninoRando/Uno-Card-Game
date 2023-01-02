package view.gameElements;

import javafx.application.Platform;
import javafx.scene.layout.StackPane;

/* --- Mine ------------------------------- */

import events.toView.EventListener;
import events.toView.EventType;

import prefabs.Card;
import prefabs.Player;

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

     private CardContainer terrainCard;

    /* --- Body ------------------------------- */

    private void initialize() {
        terrainCard = new CardContainer();
        getChildren().setAll(terrainCard);
    }

    private void updateTerrainCard(Card card) {
        terrainCard.update(card.getGuiContainer());
    }

    /* --- Observer --------------------------- */

    @Override
    public void update(EventType event, Card data) {
        switch (event) {
            case CARD_CHANGE:
                Platform.runLater(() -> updateTerrainCard(data));
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }

    @Override
    public void update(EventType event, Player[] data) {
        switch (event) {
            case GAME_READY:
                Platform.runLater(() -> initialize());
                break;
            default:
                throwUnsupportedError(event, null);
        }
    }
}