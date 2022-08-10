package view.gameElements;

import javafx.application.Platform;

import javafx.scene.layout.StackPane;

import model.gameLogic.Loop;
import model.gameLogic.Card;

import model.events.EventListener;

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
        Loop.events.subscribe(this, "cardPlayed", "firstCard", "reset");
        getStyleClass().add("terrain");
        setMaxHeight(400);
        setMaxWidth(400);

        getChildren().add(terrainCard);
    }

    /* ---------------------------------------- */
    private CardContainer terrainCard = new CardContainer();

    private void updateTerrainCard(Card c) {
        terrainCard.update(c.getGuiContainer());
    }

    private void reset() {
        terrainCard = new CardContainer();
        getChildren().clear();
        getChildren().add(terrainCard);
        Loop.events.subscribe(this, "cardPlayed", "firstCard", "reset");
    }

    @Override
    public void update(String eventLabel, Object... data) {
        switch (eventLabel) {
            case "firstCard":
            case "cardPlayed":
                Platform.runLater(() -> updateTerrainCard((Card) data[0]));
                break;
            case "reset":
                reset();
                break;
        }
    }
}