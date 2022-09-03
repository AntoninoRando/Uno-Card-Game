package view.gameElements;

import events.EventListener;
import javafx.application.Platform;

import javafx.scene.layout.StackPane;

import model.gameLogic.Loop;
import model.gameLogic.Card;

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
        Loop.events.subscribe(this, "gameStart", "cardPlayed", "firstCard");
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
    public void update(String eventLabel, Object[] data) {
        switch (eventLabel) {
            case "gameStart":
                Platform.runLater(() -> initialize());
                break;
            case "firstCard":
            case "cardPlayed":
                Platform.runLater(() -> updateTerrainCard((Card) data[0]));
                break;
        }
    }
}