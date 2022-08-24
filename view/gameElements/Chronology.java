package view.gameElements;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;

import model.events.EventListener;
import model.gameLogic.Loop;
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
            case "cardPlayed":
                Platform.runLater(() -> content.getChildren().add(new CardContainer(((Card) data[0]))));
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
