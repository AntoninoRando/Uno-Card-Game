package view.gameElements;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/* --- Mine ------------------------------- */

import events.toView.EventListener;
import events.toView.EventType;

import prefabs.Player;

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

    private Map<Player, PlayerLabel> labels;

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

    private void addPlayerLabel(Player player) {
        PlayerLabel label = new PlayerLabel(player.getIcon(), player.getNick(),
                player.getHand().size());
        getChildren().add(label);
        labels.put(player, label);
    }

    public PlayerLabel getPlayerLabel(Player player) {
        return labels.get(player);
    }

    /* --- Observer --------------------------- */

    @Override
    public void update(EventType event, Player[] data) {
        switch (event) {
            case GAME_READY:
                Platform.runLater(() -> {
                    initialize();
                    for (Player player : data)
                        addPlayerLabel(player);
                });
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }

    @Override
    public void update(EventType event, Player data) {
        switch (event) {
            case PLAYER_HAND_INCREASE:
                Platform.runLater(() -> labels.get(data).modifyHandSize(1));
                break;
            case PLAYER_HAND_DECREASE:
                Platform.runLater(() -> labels.get(data).modifyHandSize(-1));
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }
}
