package view.endGame;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import model.data.UserInfo;

public class GameResults extends VBox {
    VBox winner;
    VBox xpEarned;
    HBox buttons;

    GameResults() {
        setId("game-results");
        arrangeElements();
    }

    private void arrangeElements() {
        setAlignment(Pos.CENTER);
        setSpacing(50.0);
        getChildren().addAll(newWinner(), newXpEarned(), newButtons());
    }

    private VBox newWinner() {
        Circle icon = new Circle(47.0, 47.0, 47.0);
        ImageView border = new ImageView(new Image("resources\\WinnerBorder.png"));
        border.setPreserveRatio(true);
        border.setFitWidth(100.0);
        StackPane avatar = new StackPane(icon, border);

        Label nickname = new Label();
        VBox e = new VBox(avatar, nickname);
        e.setSpacing(10.0);
        e.setAlignment(Pos.TOP_CENTER);
        winner = e;
        return e;
    }

    private VBox newXpEarned() {
        ProgressBar xpBar = new ProgressBar((double) UserInfo.getXp() / UserInfo.getXpGap());
        xpBar.getStyleClass().add("xp-bar");
        xpBar.setPrefHeight(5.0);
        xpBar.setPrefWidth(200.0);
        Label newXp = new Label();
        VBox e = new VBox(xpBar, newXp);
        e.setAlignment(Pos.CENTER);
        xpEarned = e;
        return e;
    }

    private HBox newButtons() {
        HBox e = new HBox();
        e.setAlignment(Pos.CENTER);
        e.setSpacing(25.0);
        buttons = e;
        return e;
    }

    void updateWinner(String iconPath, String nickname) {
        ((Circle) ((StackPane) winner.getChildren().get(0)).getChildren().get(0))
                .setFill(new ImagePattern(new Image(iconPath)));
        ((Label) winner.getChildren().get(1)).setText(nickname);
    }

    void updateXpEarned(int xp) {
        ((ProgressBar) xpEarned.getChildren().get(0)).setProgress((double) UserInfo.getXp() / UserInfo.getXpGap());
        ((Label) xpEarned.getChildren().get(1)).setText("+" + Integer.toString(xp) + " xp");
    }
}
