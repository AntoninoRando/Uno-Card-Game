package view.gameElements;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.gameLogic.Loop;
import model.data.UserInfo;
import model.events.EventListener;
import model.gameLogic.Player;

public class PlayerPane extends VBox implements EventListener {
    /* SINGLETON */
    /* --------- */
    private static PlayerPane instance;

    public static PlayerPane getInstance() {
        if (instance == null)
            instance = new PlayerPane();
        return instance;
    }

    private PlayerPane() {
        addStyle();
        Loop.events.subscribe(this, "gameSetupped", "playerDrew", "playerHandChanged");
    }

    /* ---------------------------------------- */

    private Map<Player, PlayerLabel> labels;

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
        String iconPath = player.isHuman() ? UserInfo.getIconPath() : player.getIconPath();
        PlayerLabel label = new PlayerLabel(iconPath, player.getNickname(), player.getHand().size());
        getChildren().add(label);
        labels.put(player, label);
    }

    private void updatePlayerInfo(Player player) {
        labels.get(player).changeCards(player.getHand().size());
    }

    /* ------------------------------- */

    public PlayerLabel getPlayerLabel(Player player) {
        return labels.get(player);
    }

    /* ------------------------------- */

    @Override
    public void update(String eventLabel, Object... data) {
        switch (eventLabel) {
            case "gameSetupped":
                Platform.runLater(() -> {
                    initialize();
                    for (Object player : data)
                        addPlayerLabel((Player) player);
                });
                break;
            case "playerHandChanged":
            case "playerDrew":
                Platform.runLater(() -> updatePlayerInfo((Player) data[0]));
                break;
        }
    }
}
