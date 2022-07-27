package view;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import model.Loop;
import model.Player;
import model.events.EventListener;

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

        Loop.getInstance().events.subscribe(this, "gameStart", "playerDrew", "playerHandChanged", "turnStart", "turnEnd");
    }

    /* ---------------------------------------- */

    private Map<Player, Label> labels;

    private void addPlayerLabel(Player player) {
        Label playerNick = new Label();
        playerNick.getStyleClass().add("player-label");
        getChildren().add(playerNick);
        labels.put(player, playerNick);
    }

    public void updatePlayerInfo(Player player) {
        Label playerNick = labels.get(player);
        playerNick.setText(player.getNickname() + " " + player.getHand().size());
    }
    
    public void focusPlayer(Player player) {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            labels.get(player).setTextFill(Color.color(1, 0, 0));
            latch.countDown();
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
        }
    }

    public void unfocusPlayer(Player player) {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            labels.get(player).setTextFill(Color.color(0, 0, 0));
            latch.countDown();
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void update(String eventLabel, Object data) {
        switch (eventLabel) {
            case "gameStart":
                Platform.runLater(() -> ((Collection<Player>) data).forEach(this::addPlayerLabel));
                break;
            case "playerDrew":
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
}
