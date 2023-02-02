package view.gameElements;

import java.util.HashMap;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/* --- JUno ------------------------------- */

import view.CUView;
import view.media.ArcNodes;

import events.EventListener;
import events.Event;

/**
 * The section visible to the user where all their cards are gathered.
 */
public class HandPane extends HBox implements EventListener {
    /* --- Singleton -------------------------- */

    private static HandPane instance;

    public static HandPane getInstance() {
        if (instance == null)
            instance = new HandPane();
        return instance;
    }

    private HandPane() {
        CUView.getInstance().subscribe(this, Event.GAME_READY, Event.USER_PLAYED_CARD, Event.USER_DREW, Event.GAME_READY);
        
        getStyleClass().add("hand");
        setSpacing(-30.0);
        setTranslateY(40.0);
    }

    /* --- Fields ----------------------------- */

    private double cardWidth = 150.0;

    /* --- Body ------------------------------- */

    /**
     * Arranges the cards in hand so that they form an arc.
     */
    private void adjustCards() {
        // Adjust card gaps
        double w = Stage.getWindows().get(0).getWidth() - 200 * 2;
        double gap = w / getChildren().size() - cardWidth;
        gap = Double.min(-30.0, gap);
        setSpacing(gap);

        // Adjust card positions
        int i = 0;
        for (double[] xyr : new ArcNodes(getChildren().size(), 1500.0, gap, cardWidth)) {
            Node node = getChildren().get(i++);
            node.setTranslateY(xyr[1]);
            node.setRotate(xyr[2]);
        }
    }

    /* --- Observer --------------------------- */

    @Override
    public void update(Event event, HashMap<String, Object> data) {
        Card card = (Card) data.get("card-node");
        switch (event) {
            case GAME_READY:
                Platform.runLater(() -> getChildren().clear());
                break;
            case USER_DREW:
                CUView.communicate(event, data);

                Platform.runLater(() -> {
                    getChildren().add(card);
                    adjustCards();
                });
                break;
            case USER_PLAYED_CARD:
                Platform.runLater(() -> {
                    getChildren().remove(card);
                    adjustCards();
                });
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }
}
