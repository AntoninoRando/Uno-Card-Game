package view.settings;

import events.EventListener;
import events.EventType;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class ProfileMenu extends VBox implements EventListener {
    Circle avatar = createAvatar();
    TextField nickField = createNickField();
    Label levelLabel = createLevelLabel();
    ProgressBar xpBar = createXpBar();
    Label xpGapLabel = createXpGapLabel();
    Label gamesPlayedLabel = createGamesPlayedLabel();
    Label winRateLabel = createWinRateLabel();
    Button deleteButton = createDeleteButton();

    ProfileMenu() {
        setMaxHeight(500.0);
        setMaxWidth(700.0);
        setPrefWidth(700.0);
        setPrefHeight(500.0);

        arrangeElements();
        setId("profile-menu");
    }

    private Circle createAvatar() {
        Circle avatar = new Circle(20, 20, 20);
        avatar.setId("avatar");
        return avatar;
    }

    private TextField createNickField() {
        TextField nickField = new TextField();
        return nickField;
    }

    private Label createLevelLabel() {
        Label levelLabel = new Label();
        levelLabel.setId("level-label");
        return levelLabel;
    }

    private ProgressBar createXpBar() {
        ProgressBar xpBar = new ProgressBar();
        xpBar.getStyleClass().add("xp-bar");
        return xpBar;
    }

    private Label createXpGapLabel() {
        Label xpGapLabel = new Label();
        xpGapLabel.setId("xp-gap-label");
        return xpGapLabel;
    }

    private Label createGamesPlayedLabel() {
        Label gamesPlayedLabel = new Label();
        gamesPlayedLabel.setId("games-played-label");
        return gamesPlayedLabel;
    }

    private Label createWinRateLabel() {
        Label winRateLabel = new Label();
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

    @Override
    public void update(EventType event, String data) {
        switch (event) {
            case USER_NEW_NICK:
                nickField.setPromptText(data);
                break;
            case USER_NEW_ICON:
                avatar.setFill(new ImagePattern(new Image(data)));
                break;
            default:
                throwUnsupportedError(event, data);
            // case "infoResetted":
            //     // TODO
            //     break;
        }
    }

    @Override
    public void update(EventType event, int data) {
        switch (event) {
            case XP_EARNED:
                xpBar.setProgress(data);
                // xpGapLabel.setText((double) data[2] + "%");
                break;
            case LEVELED_UP:
                levelLabel.setText("Level " + data);
                break;
            case USER_PLAYED_GAME:
                gamesPlayedLabel.setText(data + "");
                break;
            case USER_WON:
                winRateLabel.setText(data + "");
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }
}
