package view.gameElements;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import view.CUView;
import events.EventListener;
import events.EventManager;
import events.EventType;

/*
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
        addStyle();
    }

    /* --- Fields ----------------------------- */

    private Set<Node> cardsStored;
    public EventManager controlsObservingMe = new EventManager();

    /* --- Body ------------------------------- */

    public void addCard(Node card) {
        getChildren().add(card);
        cardsStored.add(card);
        adjustCards();

    }

    public void removeCard(Card card) {
        getChildren().remove(card);
        cardsStored.remove(card);
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

    /* ------ Gather cards in an arc way ------- */

    // TODO fare che non calcola ogni volta le coordinate ma le calcola solo quando
    // dal menu dell'app scegli la risoluzione; a quel punto salva la lista di
    // coordinate e sfrutta quella.

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

    /* --- Observer --------------------------- */

    @Override
    public void update(EventType event, HashMap<String, Object> data) {
        switch (event) {
            case GAME_READY:
                Platform.runLater(() -> initialize());
                break;
            case USER_DREW:
                int cardTag = (int) data.get("card-tag");
                HashMap<String, Object> data2 = new HashMap<>();
                data2.put("card-node", Card.cards.get(cardTag));
                data2.put("card-tag", cardTag);
                CUView.getInstance().communicate(event, data2);
                Platform.runLater(() -> {
                    addCard(Card.cards.get(cardTag));
                });
                break;
            case USER_PLAYED_CARD:
                int cardTag2 = (int) data.get("card-tag");
                Platform.runLater(() -> removeCard(Card.cards.get(cardTag2)));
                break;
            default:
                throwUnsupportedError(event, null);
        }
    }
}
