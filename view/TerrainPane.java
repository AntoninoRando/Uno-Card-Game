package view;

import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import model.Loop;
import model.cards.Card;
import model.events.EventListener;
import view.animations.Animation;
import view.animations.AnimationLayer;
import view.animations.Animations;

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
        Loop.getInstance().events.subscribe("firstCard", this);
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
            Platform.runLater(() -> {
                Animation cardPlayed = Animations.CARD_PLAYED.get();
                cardPlayed.setOnFinishAction(e -> updateTerrainCard((Card) data));
                cardPlayed.playOnQueue(AnimationLayer.getInstance());
            });
        else if (eventType.equals("firstCard"))
            Platform.runLater(() -> updateTerrainCard((Card) data));
    }
}