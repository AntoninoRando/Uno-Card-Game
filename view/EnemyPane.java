package view;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
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

        Loop.getInstance().events.subscribe(this, "gameStart", "playerDrew", "playerHandChanged", "turnStart");
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

    public void focuEnemy(Player player) {
        // TODO
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
                Platform.runLater(() -> focuEnemy((Player) data));
                break;
        }
    }
}
