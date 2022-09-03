package view.gameElements;

import events.EventListener;
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

import model.data.PlayerData;
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
        Loop.events.subscribe(this, "firstCard", "cardPlayed", "playerDrew", "turnBlocked", "reset");
        ScrollPane sp = new ScrollPane(content);
        getChildren().add(sp);
        addStyle();
    }

    /* ---------------------------------------- */

    private HBox content = new HBox();

    private void addStyle() {
        ScrollPane sp = (ScrollPane) getChildren().get(0);
        sp.setMaxHeight(400.0);
        sp.setMaxWidth(1000.0);
        sp.setFitToHeight(true);
        sp.setFitToWidth(true);
        sp.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        sp.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setStyle("-fx-background: none");

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
    public void update(String eventLabel, Object[] data) {
        switch (eventLabel) {
            case "firstCard":
                Platform.runLater(() -> content.getChildren().add(new CardContainer(((Card) data[0]))));
                break;
            case "cardPlayed":
                PlayerData p = ((Player) data[1]).info();
                Platform.runLater(
                        () -> content.getChildren().add(new Memory((Card) data[0], p.getIcon(), p.getNick())));
                break;
            case "playerDrew":
                PlayerData p2 = ((Player) data[0]).info();
                Platform.runLater(
                        () -> content.getChildren()
                                .add(new Memory(new Card(Suit.WILD, -1), p2.getIcon(), p2.getNick())));
                break;
            case "turnBlocked":
                PlayerData p3 = ((Player) data[0]).info();
                Platform.runLater(
                        () -> content.getChildren()
                                .add(new Memory(new Card(Suit.WILD, -2), p3.getIcon(), p3.getNick())));
                break;
            case "reset":
                Platform.runLater(() -> {
                    content = new HBox();
                    ScrollPane sp = (ScrollPane) getChildren().get(0);
                    sp.setContent(content);
                });
                break;
        }
    }
}

class Memory extends VBox {
    Memory(Card card, String playerIcon, String nickname) {
        setAlignment(Pos.CENTER);
        CardContainer cardContainer = new CardContainer(card);
        Circle avatar = new Circle(30, new ImagePattern(new Image(playerIcon)));
        Label nick = new Label(nickname);
        nick.getStyleClass().add(".player-label");
        getChildren().addAll(cardContainer, avatar, nick);
        avatar.setTranslateY(-30.0);
    }
}