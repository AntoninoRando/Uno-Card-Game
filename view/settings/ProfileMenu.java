package view.settings;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

/* --- Mine ------------------------------- */

import events.EventListener;
import events.Event;

import view.GUIContainer;

import controller.Controls;

/**
 * A GUI element that displays user info and provides ways of changing them.
 */
public class ProfileMenu extends StackPane implements EventListener, GUIContainer {
    /* --- Singleton -------------------------- */

    private static ProfileMenu instance;

    public static ProfileMenu getInstance() {
        if (instance == null)
            instance = new ProfileMenu();
        return instance;
    }

    private ProfileMenu() {
        createElements();
        arrangeElements();
        applyBehaviors();
    }

    /* --- Fields ----------------------------- */

    private AvatarPicker avatarPicker;
    private VBox infoContainer;
    private Circle avatar;
    private TextField nickField;
    private Label levelLabel;
    private ProgressBar xpBar;
    private Label progressLabel;
    private Label gamesPlayedLabel;
    private Label winRateLabel;
    private Button deleteButton;

    /* ---.--- Getters and Setters ------------ */

    public AvatarPicker getAvatarPicker() {
        return avatarPicker;
    }

    /* --- Visible ---------------------------- */

    @Override
    public void createElements() {
        avatarPicker = new AvatarPicker();
        infoContainer = new VBox();
        avatar = new Circle(20, 20, 20);
        nickField = new TextField();
        levelLabel = new Label();
        xpBar = new ProgressBar();
        progressLabel = new Label();
        gamesPlayedLabel = new Label();
        winRateLabel = new Label();
        deleteButton = new Button("Delete Account");
    }

    @Override
    public void arrangeElements() {
        avatarPicker.setVisible(false);
        avatar.setId("avatar");
        levelLabel.setId("level-label");
        xpBar.getStyleClass().add("xp-bar");
        progressLabel.setId("level-progress-label");
        gamesPlayedLabel.setId("games-played-label");
        winRateLabel.setId("win-rate-label");
        deleteButton.setId("delete-button");

        setId("profile-menu");

        setMaxHeight(500.0);
        setMaxWidth(700.0);
        setPrefWidth(700.0);
        setPrefHeight(500.0);

        HBox first = new HBox(20.0, avatar, nickField, levelLabel, xpBar, progressLabel);
        HBox second = new HBox(20.0, gamesPlayedLabel, winRateLabel);
        HBox third = new HBox(deleteButton);

        infoContainer.getChildren().addAll(first, second, new HBox(), new HBox(), third);
        infoContainer.setSpacing(40.0);

        getChildren().addAll(infoContainer, avatarPicker);
    }

    @Override
    public void applyBehaviors() {
        avatar.setOnMouseClicked(e -> avatarPicker.setVisible(!avatarPicker.isVisible()));
        Controls.NICK_ENTER.apply(nickField, () -> {
            String text = nickField.getText();
            nickField.clear();
            nickField.setPromptText(text);
            requestFocus(); // Used to remove focus from the text field
            return text;
        });
    }

    /* --- Observer --------------------------- */

    @Override
    public void update(Event event, HashMap<String, Object> data) {
        switch (event) {
            case INFO_CHANGE:
                String nickname = (String) data.get("nickname");
                String icon = (String) data.get("icon");
                int level = (int) data.get("level");
                int xp = (int) data.get("xp");
                int gap = (int) data.get("xp-gap");
                int games = (int) data.get("games");
                int wins = (int) data.get("wins");

                nickField.setPromptText(nickname);
                avatar.setFill(new ImagePattern(new Image(icon)));
                levelLabel.setText("Level " + Integer.toString(level));

                double progress = ((double) xp) / ((double) gap);
                xpBar.setProgress(progress);
                progressLabel.setText(Integer.toString((int) progress * 100) + "%");

                gamesPlayedLabel.setText("Games: " + Integer.toString(games));

                int winRate = (int) (((double) wins) / ((double) games)) * 100;
                winRateLabel.setText("Win rate: " + Integer.toString(winRate) + "%");
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }
}

/**
 * Displays all icons and lets user pick one to be their new icon.
 */
class AvatarPicker extends StackPane implements GUIContainer {
    /* --- Fields ----------------------------- */

    private GridPane grid;
    private final int iconsPerLine = 5;
    private ScrollPane container;
    private Button closeButton;

    /* --- Constructors ----------------------- */

    public AvatarPicker() {
        createElements();
        arrangeElements();
    }

    /* --- Body ------------------------------- */

    /**
     * Set all the clickable icons.
     * 
     * @param iconsPaths A collection of all the icons paths.
     */
    private void addOptions(String dirPath) {
        File directoryPath = new File(dirPath);
        String[] icons = directoryPath.list();

        for (int i = 0; i < icons.length; i++) {
            String path = directoryPath.getPath() + "/" + icons[i];
            Circle icon = createIcon(path);
            GridPane.setColumnIndex(icon, i % iconsPerLine);
            GridPane.setRowIndex(icon, i / iconsPerLine);
            grid.getChildren().add(icon);

            Controls.INFO_CHANGE.apply(icon, Map.entry("icon", path));
        }
    }

    private Circle createIcon(String iconPath) {
        Circle avatar = new Circle(30, new ImagePattern(new Image(iconPath)));
        return avatar;
    }

    /* --- Visible ---------------------------- */

    @Override
    public void createElements() {
        grid = new GridPane();
        container = new ScrollPane(grid);
        closeButton = new Button("X");

        addOptions("resources/icons");
    }

    @Override
    public void arrangeElements() {
        setId("avatar-picker");
        setMaxHeight(400.0);
        setMaxWidth(400.0);
        setPrefWidth(400.0);
        setPrefHeight(400.0);

        closeButton.getStyleClass().add("button");

        getChildren().addAll(container, closeButton);
        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);
    }

    @Override
    public void applyBehaviors() {
        closeButton.setOnMouseClicked(e -> this.setVisible(false));
    }
}