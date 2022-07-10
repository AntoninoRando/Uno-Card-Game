package view;

import java.util.HashSet;
import java.util.Set;

import javafx.application.Platform;
import javafx.scene.layout.HBox;
import model.Loop;
import model.Player;
import model.cards.Card;
import model.events.EventListener;

public class HandPane extends HBox implements EventListener {
    // private final HashMap<Integer, int[]> cardPositions = (HashMap<Integer, int[]>) Stream.of(new Object[][] {
    //         { 1, new int[] { 0 } },
    //         { 2, new int[] { -2, 2 } },
    //         { 3, new int[] { -2, 0, 2 } },
    //         { 4, new int[] { -4, -2, 2, -2 } },
    //         { 5, new int[] { -4, -2, 0, 2, 4 } },
    //         { 6, new int[] { -6, -4, -2, 0, 2, 4, 6 } },
    //         { 7, new int[] { -6, -4, -2, 0, 2, 4, 6 } }
    // }).collect(Collectors.toMap(p -> (int) p[0], p -> (int[]) p[1]));
    private Set<Card> cardsStored;

    public HandPane() {
        cardsStored = new HashSet<>();
        getStyleClass().add("hand");
        Loop.getInstance().events.subscribe("playerDrew", this);
    }

    public void addCard(Card card) {
        getChildren().add(card.getGuiContainer());
        cardsStored.add(card);
        adjustCards();
    }

    private void adjustCards() {
    }

    @Override
    public void update(String eventType, Object data) {
        Platform.runLater(() -> {
            if (eventType.equals("playerDrew")) {
                Player p = (Player) data;
                if (!p.isHuman())
                    return;
                p.getHand().forEach(card -> {
                    if (!cardsStored.contains(card))
                        addCard(card);
                });
            }
        });
    }
}
