package view.gameElements;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import events.EventListener;
import events.EventType;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import model.gameLogic.Loop;
import model.gameLogic.Card;

public class SelectionPane extends HBox implements EventListener {
    /* SINGLETON */
    /* --------- */
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

        Loop.events.subscribe(this, EventType.USER_SELECTING_CARD);
    }

    //

    private CountDownLatch latch = new CountDownLatch(1);
    private Map<CardContainer, Card> options;

    public void newSelection(CardContainer... nodes) {
        getChildren().addAll(nodes);
        // for (CardContainer n : nodes)
        //     n.setOnMouseClicked(e -> {
        //         onSelectAction.accept(options.get(n));
        //         completeSelection();
        //     });

        setVisible(true);
    }

    public void completeSelection() {
        setVisible(false);
        getChildren().clear();
        latch.countDown();
        latch = new CountDownLatch(1);
        options = new HashMap<>();
    }

    @Override
    public void update(EventType event, Card[] data) {
        switch (event) {
            case USER_SELECTING_CARD:
                CardContainer[] nodes = new CardContainer[data.length];
                for (int i = 0; i < data.length; i++) {
                    CardContainer cardContainer = data[i].getGuiContainer();
                    nodes[i] = cardContainer;
                    options.put(cardContainer, data[i]);
                }
                Platform.runLater(() -> newSelection( nodes));
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
