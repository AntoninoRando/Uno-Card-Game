package view.gameElements;

import java.util.HashMap;

import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import events.EventListener;
import events.Event;

public class CardChronology extends Chronology implements EventListener {
    /* --- Singleton -------------------------- */

    private static CardChronology instance;

    public static CardChronology getInstance() {
        if (instance == null)
            instance = new CardChronology();
        return instance;
    }

    /* --- Observer --------------------------- */

    @Override
    public void update(Event event, HashMap<String, Object> data) {
        switch (event) {
            case PLAYER_PLAYED_CARD:
                Platform.runLater(() -> {
                    Card card = (Card) data.get("card-node");
                    String icon = (String) data.get("icon");
                    String nickname = (String) data.get("nickname");
                    addMemoryInfo(card, icon, nickname);
                    update();
                });
                break;
            // TODO aggiungere anche case SELECTION così che salvo in una memoria (diversa
            // dalla carta) il risultato della selezione
            case GAME_READY:
                Platform.runLater(() -> {
                    content = new HBox();
                    ScrollPane scrollPane = (ScrollPane) getChildren().get(0);
                    scrollPane.setContent(content);
                });
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }
}
