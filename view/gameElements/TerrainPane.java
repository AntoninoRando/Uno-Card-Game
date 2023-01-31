package view.gameElements;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import view.CUView;
import view.SpriteFactory;

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
        CUView.getInstance().subscribe(this, Event.CARD_CHANGE);
        
        getStyleClass().add("terrain");
        setMaxHeight(400);
        setMaxWidth(400);

        terrainCard = new ImageView();
        getChildren().setAll(terrainCard);
    }

    /* --- Fields ----------------------------- */

    private ImageView terrainCard;

    /* --- Observer --------------------------- */

    @Override
    public void update(Event event, HashMap<String, Object> data) {
        String cardString = (String) data.get("card-representation");
        switch (event) {
            case CARD_CHANGE:
                Platform.runLater(() -> SpriteFactory.getCardSprite(cardString).draw(150.0, terrainCard));
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }
}