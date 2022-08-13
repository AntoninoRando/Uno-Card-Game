package view.settings;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import model.data.UserInfo;

public class ProfileMenu extends VBox {
    protected Circle avatar = createAvatar();
    protected TextField nickField = createNickField();
    protected Label levelLabel = createLevelLabel();
    protected ProgressBar xpBar = createXpBar();
    protected Label xpGapLabel = createXpGapLabel();
    protected Label gamesPlayedLabel = createGamesPlayedLabel();
    protected Label winRateLabel = createWinRateLabel();
    protected Button deleteButton = createDeleteButton();

    protected ProfileMenu() {
        setMaxHeight(500.0);
        setMaxWidth(700.0);
        setPrefWidth(700.0);
        setPrefHeight(500.0);

        arrangeElements();
        getStyleClass().add("profile-menu");
    }

    private Circle createAvatar() {
        Circle avatar = new Circle(20, 20, 20);
        avatar.setId("avatar");
        avatar.setFill(new ImagePattern(new Image(UserInfo.getIconPath())));
        return avatar;
    }

    private TextField createNickField() {
        TextField nickField = new TextField();
        nickField.setPromptText(UserInfo.getNick());
        return nickField;
    }

    private Label createLevelLabel() {
        Label levelLabel = new Label("Level " + Integer.toString(UserInfo.getLevel()));
        levelLabel.setId("level-label");
        return levelLabel;
    }

    private ProgressBar createXpBar() {
        ProgressBar xpBar = new ProgressBar((double) UserInfo.getXp() / UserInfo.getXpGap());
        xpBar.getStyleClass().add("xp-bar");
        return xpBar;
    }

    private Label createXpGapLabel() {
        Label xpGapLabel = new Label(
                Integer.toString(UserInfo.getXp()) + " / " + Integer.toString(UserInfo.getXpGap()) + " xp");
        xpGapLabel.setId("xp-gap-label");
        return xpGapLabel;
    }

    private Label createGamesPlayedLabel() {
        Label gamesPlayedLabel = new Label(Integer.toString(UserInfo.getGames()));
        gamesPlayedLabel.setId("games-played-label");
        return gamesPlayedLabel;
    }

    private Label createWinRateLabel() {
        Label winRateLabel = new Label(Double.toString(UserInfo.getWinRate()));
        winRateLabel.setId("win-rate-label");
        return winRateLabel;
    }

    private Button createDeleteButton() {
        Button deleteButton = new Button("Delete Account");
        deleteButton.setId("delete-button");
        return deleteButton;
    }

    private void arrangeElements() {
        HBox first = new HBox();
        first.getChildren().addAll(avatar, nickField, levelLabel, xpBar, xpGapLabel);
        first.setSpacing(20.0);

        HBox second = new HBox();
        second.getChildren().addAll(gamesPlayedLabel, winRateLabel);
        second.setSpacing(20.0);

        HBox empty1 = new HBox();
        HBox empty2 = new HBox();
        HBox third = new HBox();
        third.getChildren().addAll(deleteButton);

        getChildren().addAll(first, second, empty1, empty2, third);
        setSpacing(40.0);
    }

    protected void updateInfo() {
        avatar.setFill(new ImagePattern(new Image(UserInfo.getIconPath())));
        nickField.setPromptText(UserInfo.getNick());
        levelLabel.setText("Level " + Integer.toString(UserInfo.getLevel()));
        xpBar.setProgress((double) UserInfo.getXp() / UserInfo.getXpGap());
        xpGapLabel.setText(Integer.toString(UserInfo.getXp()) + " / " + Integer.toString(UserInfo.getXpGap()) + " xp");
        gamesPlayedLabel.setText(Integer.toString(UserInfo.getGames()));
        winRateLabel.setText(Double.toString(UserInfo.getWinRate()));
    }
}
