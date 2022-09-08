package view.gameElements;

import events.EventListener;
import events.EventType;
import javafx.application.Platform;

import javafx.scene.layout.StackPane;

import model.gameLogic.Loop;
import prefabs.Card;
import prefabs.Player;

public class TerrainPane extends StackPane implements EventListener {
    /* SINGLETON */
    /* --------- */
    private static TerrainPane instance;

    public static TerrainPane getInstance() {
        if (instance == null)
            instance = new TerrainPane();
        return instance;
    }

    private TerrainPane() {
        Loop.events.subscribe(this, EventType.GAME_READY, EventType.CARD_CHANGE);
        getStyleClass().add("terrain");
        setMaxHeight(400);
        setMaxWidth(400);
    }

    private CardContainer terrainCard;

    private void initialize() {
        terrainCard = new CardContainer();
        getChildren().setAll(terrainCard);
    }

    private void updateTerrainCard(Card c) {
        terrainCard.update(c.getGuiContainer());
    }

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