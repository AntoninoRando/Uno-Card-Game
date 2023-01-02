package view.gameElements;

import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

/* --- Mine ------------------------------- */

import controller.Select;

import events.toView.EventListener;
import events.toView.EventType;

import prefabs.Card;

public class SelectionPane extends HBox implements EventListener {
    /* --- Singleton -------------------------- */

    private static SelectionPane instance;

    public static SelectionPane getInstance() {
        if (instance == null)
            instance = new SelectionPane();
        return instance;
    }

    private SelectionPane() {
        getStyleClass().add("selection-pane");
        setSpacing(20.0);
        setVisible(false);
        setAlignment(Pos.CENTER);
    }

    /* --- Fields ----------------------------- */

    private CountDownLatch latch = new CountDownLatch(1);

    /* --- Body ------------------------------- */

    public void newSelection(Card[] cards) {
        for (int i = 0; i < cards.length; i++) {
            Card data = cards[i];
            Select control= new Select();
            control.setAction(__ -> completeSelection());
            control.setControls(data);
        }
        getChildren().addAll(Stream.of(cards).map(c -> c.getGuiContainer()).toArray(CardContainer[]::new));
        setVisible(true);
    }

    public void completeSelection() {
        setVisible(false);
        getChildren().clear();
        latch.countDown();
        latch = new CountDownLatch(1);
    }

    /* --- Observer --------------------------- */

    @Override
    public void update(EventType event, Card[] data) {
        switch (event) {
            case USER_SELECTING_CARD:
                Platform.runLater(() -> newSelection(data));
                try {
                    latch.await();
                } catch (InterruptedException e) {
                }
                break;
            // TODO case "enemyTurn cardSelection":
            default:
                throwUnsupportedError(event, data);
        }
    }
}
