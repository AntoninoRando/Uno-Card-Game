package view.gameElements;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/* --- JUno ------------------------------- */

import view.CUView;
import view.GUIContainer;

import events.EventListener;

/**
 * Gathers all player labels.
 */
public class PlayerPane extends VBox implements EventListener, GUIContainer {
    /* --- Singleton -------------------------- */

    private static PlayerPane instance;

    public static PlayerPane getInstance() {
        if (instance == null)
            instance = new PlayerPane();
        return instance;
    }

    private PlayerPane() {
        CUView.getInstance().subscribe(this, "GAME_READY", "AI_PLAYED_CARD",
                "AI_DREW", "GAME_READY", "TURN_START", "USER_DREW", "USER_PLAYED_CARD");
        initialize();
    }

    /* --- Fields ----------------------------- */

    private Map<String, PlayerLabel> labels;
    private String activePlayer;

    /* --- Body ------------------------------- */

    private void addPlayerLabel(String nickname, String icon) {
        PlayerLabel label = new PlayerLabel(icon, nickname, 0);
        getChildren().add(label);
        labels.put(nickname, label);
    }

    public PlayerLabel getPlayerLabel(String nickname) {
        return labels.get(nickname);
    }

    public PlayerLabel getPlayerLabel() {
        return labels.get(activePlayer);
    }

    /* --- GUIContainer ------------------------ */

    @Override
    public void createElements() {
        labels = new HashMap<>();
    }

    @Override
    public void arrangeElements() {
        getStyleClass().add("players");
        setSpacing(10.0);

        Label title = new Label("Players");
        title.getStyleClass().add("title");
        getChildren().setAll(title);
    }

    @Override
    public void applyBehaviors() {
    }

    /* --- Observer --------------------------- */

    @Override
    public void update(String event, Map<String, Object> data) {
        switch (event) {
            case "GAME_READY":
                String[] nicknames = (String[]) data.get("all-nicknames");
                String[] icons = (String[]) data.get("all-icons");
                Platform.runLater(() -> {
                    initialize();
                    for (int i = 0; i < icons.length; i++)
                        addPlayerLabel(nicknames[i], icons[i]);
                });
                break;
            case "AI_PLAYED_CARD":
            case "USER_PLAYED_CARD":
                Platform.runLater(() -> labels.get((String) data.get("nickname")).modifyHandSize(-1));
                break;
            case "AI_DREW":
            case "USER_DREW":
                Platform.runLater(() -> labels.get((String) data.get("nickname")).modifyHandSize(1));
                break;
            case "TURN_START":
                activePlayer = (String) data.get("nickname");
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }

    @Override
    public int getEventPriority(String event) {
        return equals("TURN_START") ? 2 : 1;
    }
}
