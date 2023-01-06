package view;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.util.HashMap;

import events.EventListener;
import events.EventType;

public class GameResults extends VBox implements EventListener {
    private static GameResults instance;

    public static GameResults getInstance() {
        if (instance == null)
            instance = new GameResults();
        return instance;
    }

    private GameResults() {
        setId("game-results");
        arrangeElements();
    }


    private VBox winner;
    private Circle icon;
    private Label nick;

    private VBox xpEarned;
    private ProgressBar xpBar;
    private Label newXp;

    private HBox buttons;

    private void arrangeElements() {
        setAlignment(Pos.CENTER);
        setSpacing(50.0);
        initialize();
        getChildren().addAll(winner, xpEarned, buttons);
    }

    private void initialize() {
        newWinner();
        newXpEarned();
        newButtons();
    }

    private void newWinner() {
        icon = new Circle(47.0, 47.0, 47.0);
        ImageView border = new ImageView(new Image("resources/WinnerBorder.png"));
        border.setPreserveRatio(true);
        border.setFitWidth(100.0);
        StackPane avatar = new StackPane(icon, border);

        nick = new Label();
        winner = new VBox(avatar, nick);
        winner.setSpacing(10.0);
        winner.setAlignment(Pos.TOP_CENTER);
    }

    private void newXpEarned() {
        xpBar = new ProgressBar();
        xpBar.getStyleClass().add("xp-bar");
        xpBar.setPrefHeight(5.0);
        xpBar.setPrefWidth(200.0);
        newXp = new Label();

        xpEarned = new VBox(xpBar, newXp);
        xpEarned.setAlignment(Pos.CENTER);
    }

    private void newButtons() {
        buttons = new HBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(25.0);
    }

    public void addButtons(Button... buttons) {
        this.buttons.getChildren().addAll(buttons);
    }

    @Override
    public void update(EventType event, HashMap<String, Object> data) {
        switch (event) {
            case PLAYER_WON:
                Platform.runLater(() -> {
                    icon.setFill(new ImagePattern(new Image((String) data.get("icon"))));
                    nick.setText((String) data.get("nickname"));
                });
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }

    // @Override
    // public void update(EventType event, int data) {
    //     switch (event) {
    //         case XP_EARNED:
    //             Platform.runLater(() -> newXp.setText("+ " + data + "xp"));
    //             break;
    //         default:
    //             throwUnsupportedError(event, data);
    //     }
    // }

    // @Override
    // public void update(EventType event, double data) {
    //     switch (event) {
    //         case NEW_LEVEL_PROGRESS:
    //             Platform.runLater(() -> xpBar.setProgress(data / 100));
    //             break;
    //         default:
    //             throwUnsupportedError(event, data);
    //     }
    // }
}
