package view.gameElements;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import model.Loop;
import model.cards.Card;
import model.events.EventListener;

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

        Loop.events.subscribe(this, "humanTurn cardSelection", "reset");
    }

    /* ---------------------------------------- */
    private CountDownLatch latch = new CountDownLatch(1);
    private Map<CardContainer, Card> options;
    
    public void newSelection(Consumer<Card> onSelectAction, CardContainer... nodes) {
        getChildren().addAll(nodes);
        for (CardContainer n : nodes)
            n.setOnMouseClicked(e -> {
                onSelectAction.accept(options.get(n));
                completeSelection();
            });

        setVisible(true);
    }

    public void completeSelection() {
        setVisible(false);
        getChildren().clear();
        latch.countDown();
        latch = new CountDownLatch(1);
    }
    
    private void reset() {
        Loop.events.subscribe(this, "humanTurn cardSelection", "reset");
    }

    @Override
    public void update(String eventLabel, Object data) {
    }

    @Override
    public void update(String eventLabel, Object... data) {
        switch (eventLabel) {
            case "humanTurn cardSelection":
                Consumer<Card> onSelect = (Consumer<Card>) data[0];
                CardContainer[] nodes = new CardContainer[data.length - 1];
                options = new HashMap<>();
                for (int i = 1; i < data.length; i++) {
                    Card c = ((Card) data[i]);
                    CardContainer cc = c.getGuiContainer();
                    options.put(cc, c);
                    nodes[i-1] = cc;
                }
                Platform.runLater(() -> newSelection(onSelect, nodes));
                try {
                    latch.await();
                } catch (InterruptedException e) {
                }
                break;
            // TODO case "enemyTurn cardSelection"
            case "reset":
                reset();
                break;
        }
    }
}