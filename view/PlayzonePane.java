package view;

import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import model.Loop;
import model.cards.Card;
import model.events.EventListener;

public class PlayzonePane extends StackPane implements EventListener {
    /* SINGLETON */
    /* --------- */
    private static PlayzonePane instance;

    public static PlayzonePane getInstance() {
        if (instance == null)
            instance = new PlayzonePane();
        return instance;
    }

    private PlayzonePane() {
        Loop.getInstance().events.subscribe("cardPlayed", this);
        getStyleClass().add("playzone");
        setMaxHeight(400);
        setMaxWidth(400);
    }

    /* ---------------------------------------- */
    CardContainer terrainCard;

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
