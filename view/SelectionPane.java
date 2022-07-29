package view;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import javafx.application.Platform;
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

        Loop.getInstance().events.subscribe(this, "humanTurn cardSelection");
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

    @Override
    public void update(String eventType, Object data) {
        switch (eventType) {
            case "humanTurn cardSelection":
                Object[] info = (Object[]) data;
                Consumer<Card> onSelect = (Consumer<Card>) info[0];
                CardContainer[] nodes = new CardContainer[info.length - 1];
                options = new HashMap<>();
                for (int i = 1; i < info.length; i++) {
                    Card c = ((Card) info[i]);
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
        }
    }
}
