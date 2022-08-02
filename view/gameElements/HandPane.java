package view.gameElements;

import java.util.HashSet;
import java.util.Set;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Loop;
import model.Player;
import model.cards.Card;
import model.events.EventListener;

public class HandPane extends HBox implements EventListener {
    /* SINGLETON */
    /* --------- */
    private static HandPane instance;

    public static HandPane getInstance() {
        if (instance == null)
            instance = new HandPane();
        return instance;
    }

    private HandPane() {
        cardsStored = new HashSet<>();
        getStyleClass().add("hand");
        Loop.events.subscribe(this, "playerDrew", "reset");
        setSpacing(-30.0);
        setTranslateY(40.0);
        assignCenter(1500.0);
    }

    /* ---------------------------------------- */
    private Set<CardContainer> cardsStored;

    public void addCard(Card card) {
        getChildren().add(card.getGuiContainer());
        cardsStored.add(card.getGuiContainer());
        adjustCards();
    }

    public void removeCard(CardContainer cardGuContainer) {
        getChildren().remove(cardGuContainer);
        cardsStored.remove(cardGuContainer);
        adjustCards();
    }

    private void reset() {
        cardsStored.clear();
        getChildren().clear();
        Loop.events.subscribe(this, "playerDrew", "reset");
    }

    /* ----------------------------------------- */

    private double cardsGap, handW;
    private double cardWidth = 150.0;

    private void adjustCardsGap() {
        handW = Stage.getWindows().get(0).getWidth() - 200 * 2;
        // handW = (cardsGap + cardWith) * cardsNumber
        double gap = handW / cardsStored.size() - cardWidth;
        if (gap >= -30)
            gap = -30;
        setSpacing(gap);
    }

    private final double handX = 0;
    private final double handY = 0;
    private double centerX, centerY, radius;

    private void assignCenter(double y) {
        centerX = handX;
        centerY = y;
        radius = y;
    }

    private double getNodeX(Node node) {
        int position = getChildren().indexOf(node) + 1;
        int n = cardsStored.size();
        // TODO la calcola male la posizione, infatti il problema della curvatura si
        // trova qua
        position = position - (n % 2 == 0 ? n / 2 : (n + 1) / 2);
        return (cardsGap + cardWidth / 2) * position;
    }

    private double getNodeY(Node node) {
        return handY;
    }

    private double findOnCircleY(Node node) {
        // (x-x0)^2 + (y-y0)^2 = r^2 -> y = y0 +- sqrt{-(x-x0)^2 + r^2}
        double nodeX = getNodeX(node);
        double sol = centerY - Math.sqrt(-Math.pow(nodeX - centerX, 2) + Math.pow(radius, 2));
        return sol; // TODO scegliere la soluzione corretta
    }

    // TODO Fare caching dei valori così da non doverli ripetere e metterli in una
    // variabile final.
    private void adjustNodeY(Node node) {
        node.setTranslateY(findOnCircleY(node) - getNodeY(node));
    }

    private void pointCenter(Node node) {
        double angle1 = Math.atan2(handY - centerY, handX - centerX);
        double angle2 = Math.atan2(getNodeY(node) - centerY, getNodeX(node) - centerX);
        node.setRotate(Math.toDegrees(angle2 - angle1));
    }

    private void adjustCards() {
        adjustCardsGap();
        for (Node node : getChildren()) {
            adjustNodeY(node);
            pointCenter(node);
        }
    }

    @Override
    public void update(String eventLabel, Object data) {
    }

    @Override
    public void update(String eventLabel, Object... data) {
        switch (eventLabel) {
            case "playerDrew":
                Platform.runLater(() -> {
                    if (((Player) data[0]).isHuman())
                        addCard((Card) data[1]);
                });
                break;
            case "reset":
                reset();
                break;
        }
    }
}
