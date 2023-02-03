package view.settings;

import java.util.Map;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/* --- Mine ------------------------------- */

import events.EventListener;
import events.Event;
import view.CUView;
import view.GUIContainer;
import view.Sprite;
import view.SpriteFactory;
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
        CUView.getInstance().subscribe(this, Event.INFO_CHANGE);
        
        initialize();
    }

    /* --- Fields ----------------------------- */

    private AvatarPicker avatarPicker;
    private VBox infoContainer;
    private ImageView avatar;
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

    /* --- GUIContainer ----------------------- */

    @Override
    public void createElements() {
        avatarPicker = new AvatarPicker();
        infoContainer = new VBox();
        avatar = new ImageView();
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
        setId("profile-menu");
        
        avatarPicker.setVisible(false);
        avatar.setId("avatar");
        levelLabel.setId("level-label");
        xpBar.getStyleClass().add("xp-bar");
        progressLabel.setId("level-progress-label");
        gamesPlayedLabel.setId("games-played-label");
        winRateLabel.setId("win-rate-label");
        deleteButton.setId("delete-button");

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
        Controls.applyNickEnter(nickField, () -> {
            String text = nickField.getText();
            nickField.clear();
            requestFocus(); // Used to remove focus from the text field
            return text;
        });
        Controls.applyInfoReset(deleteButton);
    }

    /* --- Observer --------------------------- */

    @Override
    public void update(Event event, Map<String, Object> data) {
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
                SpriteFactory.getAvatarSprite(icon).draw(50.0, avatar);
                levelLabel.setText("Level " + Integer.toString(level));

                double progress = ((double) xp) / ((double) gap);
                xpBar.setProgress(progress);
                progressLabel.setText(Integer.toString((int) (progress * 100)) + "%");

                gamesPlayedLabel.setText("Games: " + Integer.toString(games));

                int winRate = (int) ((((double) wins) / ((double) games)) * 100);
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
        initialize();
    }

    /* --- Body ------------------------------- */

    /**
     * Set all the clickable icons.
     * 
     */
    private void addOptions() {
        Sprite[] icons = SpriteFactory.getAllAvatars();

        for (int i = 0; i < icons.length; i++) {
            ImageView icon = new ImageView();
            icons[i].draw(50.0, icon);
            GridPane.setColumnIndex(icon, i % iconsPerLine);
            GridPane.setRowIndex(icon, i / iconsPerLine);
            grid.getChildren().add(icon);
            Controls.applyInfoChange(icon, Map.entry("icon", icons[i].getName()));
        }
    }

    /* --- GUIContainer ----------------------- */

    @Override
    public void createElements() {
        grid = new GridPane();
        container = new ScrollPane(grid);
        closeButton = new Button("X");
    }

    @Override
    public void arrangeElements() {
        setId("avatar-picker");
        setMaxHeight(400.0);
        setMaxWidth(400.0);
        setPrefWidth(400.0);
        setPrefHeight(400.0);

        addOptions();

        closeButton.getStyleClass().add("button");

        getChildren().addAll(container, closeButton);
        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);
    }

    @Override
    public void applyBehaviors() {
        closeButton.setOnMouseClicked(e -> this.setVisible(false));
    }
}