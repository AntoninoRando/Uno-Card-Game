package view;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.util.HashMap;

/* --- Mine ------------------------------- */

import events.EventListener;
import events.Event;

public class GameResults extends VBox implements EventListener, GUIContainer {
    /* --- Singleton -------------------------- */

    private static GameResults instance;

    public static GameResults getInstance() {
        if (instance == null)
            instance = new GameResults();
        return instance;
    }

    private GameResults() {
        initialize();
    }

    /* --- Fields ----------------------------- */

    private VBox winner;
    private Circle icon;
    private Label nick;

    private VBox xpEarned;
    private ProgressBar xpBar;
    private Label newXp;

    /* --- Visible ---------------------------- */

    @Override
    public void createElements() {
        winner = new VBox();
        icon = new Circle();
        nick = new Label();
        xpBar = new ProgressBar();
        newXp = new Label();
        xpEarned = new VBox();

    }

    @Override
    public void arrangeElements() {
        setAlignment(Pos.CENTER);
        setSpacing(50.0);
        getChildren().addAll(winner, xpEarned);

        xpBar.getStyleClass().add("xp-bar");
        xpBar.setPrefHeight(5.0);
        xpBar.setPrefWidth(200.0);

        xpEarned.getChildren().setAll(xpBar, newXp);
        xpEarned.setAlignment(Pos.CENTER);

        icon.setRadius(47.0);
        ImageView border = new ImageView(new Image("resources/WinnerBorder.png"));
        border.setPreserveRatio(true);
        border.setFitWidth(100.0);
        StackPane avatar = new StackPane(icon, border);

        winner.getChildren().setAll(avatar, nick);
        winner.setSpacing(10.0);
        winner.setAlignment(Pos.TOP_CENTER);
    }

    @Override
    public void applyBehaviors() {
    }

    /* --- Observer --------------------------- */

    @Override
    public void update(Event event, HashMap<String, Object> data) {
        switch (event) {
            case INFO_CHANGE:
                Platform.runLater(() -> {
                    int xp = (int) data.get("xp");
                    int xpGap = (int) data.get("xp-gap");
                    xpBar.setProgress(((double) xp) / ((double) xpGap));

                    data.putIfAbsent("xp-earned", 0);
                    int xpEarned = (int) data.get("xp-earned");
                    newXp.setText(Integer.toString(xpEarned));
                });
                break;
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
}
