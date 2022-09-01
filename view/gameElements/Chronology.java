package view.gameElements;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import model.events.EventListener;
import model.gameLogic.Loop;
import model.gameLogic.Player;
import model.gameLogic.Card;

public class Chronology extends ScrollPane implements EventListener {
    /* SINGLETON */
    /* --------- */
    private static Chronology instance;

    public static Chronology getInstance() {
        if (instance == null)
            instance = new Chronology();
        return instance;
    }

    private Chronology() {
        Loop.events.subscribe(this, "firstCard", "cardPlayed", "reset");
        addStyle();
        setContent(content);
    }

    /* ---------------------------------------- */

    private HBox content = new HBox();

    private void addStyle() {
        setMaxHeight(400.0);
        setMaxWidth(1000.0);
        setFitToHeight(true);
        setFitToWidth(true);
        hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);

        content.setId("chronology");
        content.setAlignment(Pos.CENTER_LEFT);
        setStyle("-fx-background: none");
    }

    public void scroll(double deltaY) {
        setHvalue(getHvalue() + (deltaY < 0 ? 0.1 : -0.1));
    }

    public void bringToTheEnd() {
        setHvalue(getHmax());
    }

    @Override
    public void update(String eventLabel, Object... data) {
        switch (eventLabel) {
            case "firstCard":
                Platform.runLater(() -> content.getChildren().add(new CardContainer(((Card) data[0]))));
                break;
            case "cardPlayed":
                Player p = (Player) data[1];
                Platform.runLater(() -> content.getChildren().add(new Memory((Card) data[0], p.getIconPath(), p.getNickname())));
                break;
            case "reset":
                Platform.runLater(() -> {
                    content = new HBox();
                    setContent(content);
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