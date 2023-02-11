package view.gameElements;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/* --- Mine ------------------------------- */

import java.util.Map;

import events.EventListener;

import view.CUView;
import view.SpriteFactory;

/**
 * A GUI element representing the current card on the ground.
 */
public class TerrainPane extends StackPane implements EventListener {
    /* --- Singleton -------------------------- */

    private static TerrainPane instance;

    
    /** 
     * @return TerrainPane
     */
    public static TerrainPane getInstance() {
        if (instance == null)
            instance = new TerrainPane();
        return instance;
    }

    private TerrainPane() {
        CUView.getInstance().subscribe(this, "CARD_CHANGE");
        
        getStyleClass().add("terrain");
        setMaxHeight(400);
        setMaxWidth(400);

        terrainCard = new ImageView();
        getChildren().setAll(terrainCard);
    }

    /* --- Fields ----------------------------- */

    private ImageView terrainCard;

    
    /** 
     * @param event
     * @param data
     */
    /* --- Observer --------------------------- */

    @Override
    public void update(String event, Map<String, Object> data) {
        String cardString = (String) data.get("card-representation");
        switch (event) {
            case "CARD_CHANGE":
                Platform.runLater(() -> SpriteFactory.getCardSprite(cardString).draw(150.0, terrainCard));
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }
}