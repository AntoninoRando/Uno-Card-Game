package view;

import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import model.Loop;
import model.cards.Card;
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
        Loop.getInstance().events.subscribe("cardPlayed", this);
        getStyleClass().add("terrain");
        setMaxHeight(400);
        setMaxWidth(400);
    }

    /* ---------------------------------------- */
    CardContainer terrainCard;

    private void updateTerrainCard(Card c) {
        terrainCard = new CardContainer(c);
        getChildren().clear();
        getChildren().add(terrainCard);
    }

    @Override
    public void update(String eventType, Object data) {
        if (eventType.equals("cardPlayed")) 
            Platform.runLater(() -> updateTerrainCard((Card) data));
    }
}