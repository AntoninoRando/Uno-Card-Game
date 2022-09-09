package view.gameElements;

import java.util.concurrent.CountDownLatch;

import events.EventListener;
import events.EventType;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import model.gameLogic.Loop;
import prefabs.Card;

public class SelectionPane extends HBox implements EventListener {
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

    //

    private CountDownLatch latch = new CountDownLatch(1);

    public void newSelection(Card[] cards) {
        CardContainer[] cardContainers = new CardContainer[cards.length];
        for (int i = 0; i < cards.length; i++) {
            cardContainers[i] = cards[i].getGuiContainer();
            int j = i;
            cardContainers[i].setOnMouseClicked(e -> {
                Loop.getInstance().completeSelectionEvent(cards[j]); // TODO Non usare il Loop qui
                completeSelection();
            });
        }
        getChildren().addAll(cardContainers);
        setVisible(true);
    }

    public void completeSelection() {
        setVisible(false);
        getChildren().clear();
        latch.countDown();
        latch = new CountDownLatch(1);
    }

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
