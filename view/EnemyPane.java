package view;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.Player;

public class EnemyPane extends VBox{
    public EnemyPane() {
        getStyleClass().add("enemies");
        setSpacing(10.0);

        Label title = new Label("Enemies");
        title.getStyleClass().add("title");
        getChildren().add(title);
    }

    public void addEnemy(Player enemy) {
        Label playerNick = new Label(enemy.getNickname() + " " + enemy.getHand().size());
        playerNick.getStyleClass().add("enemy");
        getChildren().add(playerNick);
    }
}
