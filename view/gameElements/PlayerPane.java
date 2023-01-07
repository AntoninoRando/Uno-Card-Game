package view.gameElements;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import events.EventListener;
import events.Event;

public class PlayerPane extends VBox implements EventListener {
    /* --- Singleton -------------------------- */

    private static PlayerPane instance;

    public static PlayerPane getInstance() {
        if (instance == null)
            instance = new PlayerPane();
        return instance;
    }

    private PlayerPane() {
        addStyle();
    }

    /* --- Fields ----------------------------- */

    private Map<String, PlayerLabel> labels;

    /* --- Body ------------------------------- */

    private void addStyle() {
        getStyleClass().add("players");
        setSpacing(10.0);
    }

    private void initialize() {
        labels = new HashMap<>();
        Label title = new Label("Players");
        title.getStyleClass().add("title");
        getChildren().setAll(title);
    }

    private void addPlayerLabel(String nickname, String icon, int handSize) {
        PlayerLabel label = new PlayerLabel(icon, nickname, handSize);
        getChildren().add(label);
        labels.put(nickname, label);
    }

    public PlayerLabel getPlayerLabel(String nickname) {
        return labels.get(nickname);
    }

    /* --- Observer --------------------------- */

    @Override
    public void update(Event event, HashMap<String, Object> data) {
        switch (event) {
            case GAME_READY:
                String[] nicknames = (String[]) data.get("all-nicknames");
                String[] icons = (String[]) data.get("all-icons");
                int[] hands = (int[]) data.get("all-hand-sizes");
                Platform.runLater(() -> {
                    initialize();
                    for (int i = 0; i < icons.length; i++)
                        addPlayerLabel(nicknames[i], icons[i], hands[i]);
                });
                break;
            case PLAYER_HAND_INCREASE:
                Platform.runLater(() -> labels.get((String) data.get("nickname")).modifyHandSize(1));
                break;
            case PLAYER_HAND_DECREASE:
                Platform.runLater(() -> labels.get((String) data.get("nickname")).modifyHandSize(-1));
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }
}
