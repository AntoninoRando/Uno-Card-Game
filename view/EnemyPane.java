package view;

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
        labels = new HashMap<>(3);

        getStyleClass().add("enemies");
        setSpacing(10.0);

        Label title = new Label("Enemies");
        title.getStyleClass().add("title");
        getChildren().add(title);

        Loop.getInstance().events.subscribe("playerDrew", this);
        Loop.getInstance().events.subscribe("playerHandChanged", this);
    }

    /* ---------------------------------------- */

    private Map<Player, Label> labels;

    public void addEnemy(Player enemy) {
        Label playerNick = new Label(enemy.getNickname() + " " + enemy.getHand().size());
        playerNick.getStyleClass().add("enemy");

        if (labels.containsKey(enemy))
            getChildren().remove(labels.get(enemy)); // removing the old label if present

        getChildren().add(playerNick);
        labels.put(enemy, playerNick);
    }

    @Override
    public void update(String eventType, Object data) {
        Platform.runLater(() -> {
            if (eventType.equals("playerDrew") || eventType.equals("playerHandChanged"))
                addEnemy((Player) data);
        });
    }
}
