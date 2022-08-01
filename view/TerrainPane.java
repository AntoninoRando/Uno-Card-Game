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
        Loop.events.subscribe(this, "enemyTurn cardPlayed", "humanTurn cardPlayed", "firstCard", "reset");
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

    private void reset() {
        terrainCard = null;
        getChildren().clear();
        Loop.events.subscribe(this, "enemyTurn cardPlayed", "humanTurn cardPlayed", "firstCard", "reset");
    }

    @Override
    public void update(String eventLabel, Object data) {
        switch (eventLabel) {
            case "enemyTurn cardPlayed":
                Platform.runLater(() -> {
                    Animation cardPlayed = Animations.CARD_PLAYED.get();
                    cardPlayed.setOnFinishAction(e -> updateTerrainCard((Card) data));
                    cardPlayed.playAndWait(AnimationLayer.getInstance());
                });
                try {
                    Animation.latch.await();
                } catch (InterruptedException e) {
                }
                break;
            case "firstCard":
            case "humanTurn cardPlayed":
                Platform.runLater(() -> updateTerrainCard((Card) data));
                break;
        }
    }

    @Override
    public void update(String eventLabel, Object... data) {
        switch (eventLabel) {
            case "reset":
                reset();
                break;
        }
    }
}