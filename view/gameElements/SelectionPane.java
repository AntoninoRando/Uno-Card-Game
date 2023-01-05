package view.gameElements;

import java.util.concurrent.CountDownLatch;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

/* --- Mine ------------------------------- */

import controller.Select;
import events.EventListener;
import events.EventType;

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

    public void newSelection(int[] cardTags) {
        // for (int i = 0; i < cardTags.length; i++) {
        //     int tag = cardTags[i];
        //     Select control= new Select();
        //     control.setAction(__ -> completeSelection());

        //     Card card = Card.cards.get(tag);
        //     control.setControls(card, tag);
        //     getChildren().add(card);
        // }
        // // getChildren().addAll(Stream.of(cardTags).map(tag -> CardContainer.cards.get(tag)).toArray(CardContainer[]::new));
        // setVisible(true);
    }

    public void completeSelection() {
        setVisible(false);
        getChildren().clear();
        latch.countDown();
        latch = new CountDownLatch(1);
    }

    /* --- Observer --------------------------- */

    @Override
    public void update(EventType event, int[] cardTags) {
        switch (event) {
            case USER_SELECTING_CARD:
                Platform.runLater(() -> newSelection(cardTags));
                try {
                    latch.await();
                } catch (InterruptedException e) {
                }
                break;
            // TODO case "enemyTurn cardSelection":
            default:
                throwUnsupportedError(event, cardTags);
        }
    }
}
