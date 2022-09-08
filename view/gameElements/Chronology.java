package view.gameElements;

import events.EventListener;
import events.EventType;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import model.gameLogic.Loop;
import model.gameLogic.Player;
import model.gameLogic.Suit;
import model.gameLogic.Card;

public class Chronology extends StackPane implements EventListener {
    /* SINGLETON */
    /* --------- */
    private static Chronology instance;

    public static Chronology getInstance() {
        if (instance == null)
            instance = new Chronology();
        return instance;
    }

    private Chronology() {
        Loop.events.subscribe(this, EventType.CARD_CHANGE, EventType.TURN_BLOCKED, EventType.PLAYER_DREW,
                EventType.RESET);
        ScrollPane sp = new ScrollPane(content);
        getChildren().add(sp);
        addStyle();
    }

    /* ---------------------------------------- */

    private HBox cardLine = new HBox();
    private HBox iconLine = new HBox();
    private HBox nickLine = new HBox();
    private VBox content = new VBox(cardLine, iconLine, nickLine);

    private void addCard(CardContainer card) {
        cardLine.getChildren().add(card);
    }

    private void addIcon(String icon) {
        Circle avatar = new Circle(30, new ImagePattern(new Image(icon)));
        iconLine.getChildren().add(avatar);
    }

    private void addNick(String nick) {
        Label nickL = new Label(nick);
        nickLine.getChildren().add(nickL);
    }

    private void addStyle() {
        ScrollPane sp = (ScrollPane) getChildren().get(0);
        sp.setMaxHeight(400.0);
        sp.setMaxWidth(1000.0);
        sp.setFitToHeight(true);
        sp.setFitToWidth(true);
        sp.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        sp.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);

        content.setAlignment(Pos.CENTER_LEFT);

        setId("chronology");
    }

    public void scroll(double deltaY) {
        ScrollPane sp = (ScrollPane) getChildren().get(0);
        sp.setHvalue(sp.getHvalue() + (deltaY < 0 ? 0.1 : -0.1));
    }

    public void bringToTheEnd() {
        ScrollPane sp = (ScrollPane) getChildren().get(0);
        sp.setHvalue(sp.getHmax());
    }

    @Override
    public void update(EventType event, Card data) {
        switch (event) {
            case CARD_CHANGE:
                Platform.runLater(() -> addCard(data.getGuiContainer()));
                break;
            default:
                // TODO risolvere throwUnsupportedError(event, data);
                break;
        }
    }

    @Override
    public void update(EventType event, Player data) {
        switch (event) {
            case CARD_CHANGE:
                Platform.runLater(() -> {
                    addIcon(data.info().getIcon());
                    addNick(data.info().getNick());
                });
                break;
            case TURN_BLOCKED:
                Platform.runLater(() -> {
                    addCard(new Card(Suit.WILD, -2).getGuiContainer());
                    addIcon(data.info().getIcon());
                    addNick(data.info().getNick());
                });
                break;
            case PLAYER_DREW:
                Platform.runLater(() -> {
                    addCard(new Card(Suit.WILD, -1).getGuiContainer());
                    addIcon(data.info().getIcon());
                    addNick(data.info().getNick());
                });
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }

    @Override
    public void update(EventType event) {
        switch (event) {
            case RESET:
                Platform.runLater(() -> {
                    cardLine = new HBox();
                    iconLine = new HBox();
                    nickLine = new HBox();
                    content = new VBox(cardLine, iconLine, nickLine);
                    ScrollPane sp = (ScrollPane) getChildren().get(0);
                    sp.setContent(content);
                });
                break;
            default:
                throwUnsupportedError(event, null);
        }
    }
}