package view.gameElements;

import java.util.HashMap;

import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import view.SpriteFactory;
import view.prefabs.Chronology;
import events.EventListener;
import events.Event;

public class ActionsChronology extends Chronology implements EventListener {
    /* --- Singleton -------------------------- */

    private static ActionsChronology instance;

    public static ActionsChronology getInstance() {
        if (instance == null)
            instance = new ActionsChronology();
        return instance;
    }

    /* --- Observer --------------------------- */

    @Override
    public void update(Event event, HashMap<String, Object> data) {
        String cardString = (String) data.get("card-representation");
        switch (event) {
            case GAME_READY:
                Platform.runLater(() -> {
                    content.getChildren().clear();
                    ((ScrollPane) getChildren().get(0)).setContent(content);
                });
                break;
            case PLAYER_PLAYED_CARD:
                Platform.runLater(() -> {
                    ImageView cardPlayed = new ImageView();
                    SpriteFactory.getCardSprite(cardString).draw(150.0, cardPlayed);
                    String icon = (String) data.get("icon");
                    String nickname = (String) data.get("nickname");
                    addMemoryInfo(cardPlayed, icon, nickname);
                    update();
                });
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }
}
