package view.gameElements;

import java.util.HashSet;
import java.util.Set;

import events.EventType;
import events.EventListener;
import javafx.application.Platform;

import javafx.scene.Node;
import javafx.scene.layout.HBox;

import javafx.stage.Stage;

import model.gameLogic.Loop;
import model.gameLogic.Card;
import model.gameLogic.Player;

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
        Loop.events.subscribe(this, EventType.GAME_READY, EventType.USER_PLAYED_CARD, EventType.USER_DREW);
        addStyle();
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

    private void initialize() {
        cardsStored = new HashSet<>();
        getChildren().clear();
    }

    private void addStyle() {
        getStyleClass().add("hand");
        setSpacing(-30.0);
        setTranslateY(40.0);
        assignCenter(1500.0);
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
        // Change the center to modify the hand curvature
        centerX = handX;
        centerY = y;
        radius = y;
    }

    private double getNodeX(Node node) {
        int position = getChildren().indexOf(node) + 1;
        int totalCards = cardsStored.size();
        // TODO la calcola male la posizione, infatti il problema della curvatura si
        // trova qua
        position = position - (totalCards % 2 == 0 ? totalCards / 2 : (totalCards + 1) / 2);
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
    private void adjustY(Node node) {
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
            adjustY(node);
            pointCenter(node);
        }
    }

    /* ----------------------------------------- */

    @Override
    public void update(EventType event, Player[] data) {
        switch (event) {
            case GAME_READY:
                Platform.runLater(() -> initialize());
                break;
            default:
                throwUnsupportedError(event, null);
        }
    }

    @Override
    public void update(EventType event, Card data) {
        switch (event) {
            case USER_DREW:
                Platform.runLater(() -> addCard(data));
                break;
            case USER_PLAYED_CARD:
                Platform.runLater(() -> removeCard(data.getGuiContainer()));
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }
}
