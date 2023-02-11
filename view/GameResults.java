package view;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Map;

/* --- JUno ------------------------------- */

import events.EventListener;

/**
 * A GUI element showing the just-played match recap.
 */
public class GameResults extends VBox implements EventListener, GUIContainer {
    /* --- Singleton -------------------------- */

    private static GameResults instance;

    
    /** 
     * @return GameResults
     */
    public static GameResults getInstance() {
        if (instance == null)
            instance = new GameResults();
        return instance;
    }

    private GameResults() {
        CUView.getInstance().subscribe(this, "PLAYER_WON", "INFO_CHANGE");
        initialize();
    }

    /* --- Fields ----------------------------- */

    private VBox winner;
    private ImageView icon;
    private Label nick;
    private VBox xpEarned;
    private ProgressBar xpBar;
    private Label newXp;

    /* --- GUIContainer ------------------------ */

    @Override
    public void createElements() {
        winner = new VBox();
        icon = new ImageView();
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

    
    /** 
     * @param event
     * @param data
     */
    /* --- Observer --------------------------- */

    @Override
    public void update(String event, Map<String, Object> data) {
        switch (event) {
            case "INFO_CHANGE":
                Platform.runLater(() -> {
                    int xp = (int) data.get("xp");
                    int xpGap = (int) data.get("xp-gap");
                    xpBar.setProgress(((double) xp) / ((double) xpGap));

                    data.putIfAbsent("xp-earned", 0);
                    int xpEarned = (int) data.get("xp-earned");
                    newXp.setText(Integer.toString(xpEarned));
                });
                break;
            case "PLAYER_WON":
                Platform.runLater(() -> {
                    SpriteFactory.getAvatarSprite((String) data.get("icon")).draw(100.0, icon);
                    nick.setText((String) data.get("nickname"));
                });
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }
}
