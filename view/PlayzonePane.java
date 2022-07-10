package view;

import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import model.Loop;
import model.cards.Card;
import model.events.EventListener;

public class PlayzonePane extends StackPane implements EventListener {
    CardContainer terrainCard;

    public PlayzonePane() {
        Loop.getInstance().events.subscribe("cardPlayed", this);
        getStyleClass().add("playzone");
        setMaxHeight(400);
        setMaxWidth(400);
    }

    private void updateTerrainCard(Card c) {
        terrainCard = new CardContainer(c);
        getChildren().add(terrainCard);
    }

    @Override
    public void update(String eventType, Object data) {
        if (eventType.equals("cardPlayed"))
            Platform.runLater(() -> updateTerrainCard((Card) data));
    }
}
