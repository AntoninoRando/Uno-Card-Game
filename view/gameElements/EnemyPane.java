package view.gameElements;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.gameLogic.Loop;
import model.data.UserInfo;
import model.events.EventListener;
import model.gameLogic.Player;

public class EnemyPane extends VBox implements EventListener {
    /* SINGLETON */
    /* --------- */
    private static EnemyPane instance;

    public static EnemyPane getInstance() {
        if (instance == null)
            instance = new EnemyPane();
        return instance;
    }

    private EnemyPane() {
        labels = new HashMap<>();

        getStyleClass().add("players");
        setSpacing(10.0);

        Label title = new Label("Players");
        title.getStyleClass().add("title");
        getChildren().add(title);

        Loop.events.subscribe(this, "gameStart", "playerDrew", "playerHandChanged", "turnStart",
                "turnEnd", "reset");
    }

    /* ---------------------------------------- */

    private Map<Player, PlayerLabel> labels;

    private void addPlayerLabel(Player player) {
        String iconPath = player.isHuman() ? UserInfo.getIconPath()
                : PlayerLabel.botIcons.getOrDefault(player.getNickname(), "resources\\icons\\night.png");
        PlayerLabel label = new PlayerLabel(iconPath, player.getNickname(), player.getHand().size());
        getChildren().add(label);
        labels.put(player, label);
    }

    private void updatePlayerInfo(Player player) {
        labels.get(player).changeCards(player.getHand().size());
    }

    private void focusPlayer(Player player) {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            labels.get(player).focusPlayer();
            latch.countDown();
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
        }
    }

    private void unfocusPlayer(Player player) {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            labels.get(player).unfocusPlayer();
            latch.countDown();
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
        }
    }

    private void reset() {
        labels.clear();
        getChildren().clear();
        Label title = new Label("Players");
        title.getStyleClass().add("title");
        getChildren().add(title);
        Loop.events.subscribe(this, "gameStart", "playerDrew", "playerHandChanged", "turnStart",
                "turnEnd", "reset");
    }

    @Override
    public void update(String eventLabel, Object data) {
        switch (eventLabel) {
            case "playerHandChanged":
                Platform.runLater(() -> updatePlayerInfo((Player) data));
                break;
            case "turnStart":
                focusPlayer((Player) data);
                break;
            case "turnEnd":
                unfocusPlayer((Player) data);
                break;
        }
    }

    @Override
    public void update(String eventLabel, Object... data) {
        switch (eventLabel) {
            case "gameStart":
                Platform.runLater(() -> {
                    for (Object player : data)
                        addPlayerLabel((Player) player);
                });
                break;
            case "playerDrew":
                Platform.runLater(() -> updatePlayerInfo((Player) data[0]));
                break;
            case "reset":
                reset();
                break;
        }
    }
}
