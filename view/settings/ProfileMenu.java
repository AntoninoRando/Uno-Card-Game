package view.settings;

import java.util.HashMap;
import java.util.function.Consumer;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import events.EventListener;
import events.EventType;

/**
 * A GUI element displaying user info, which provides ways of changing user info.
 */
public class ProfileMenu extends StackPane implements EventListener {
    private static ProfileMenu instance;

    public static ProfileMenu getInstance() {
        if (instance == null)
            instance = new ProfileMenu();
        return instance;
    }

    private ProfileMenu() {
        setId("profile-menu");
        initialize();
        arrangeElements();
    }

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

    private void initialize() {
        newAvatarPicker();
        newInfoContainer();
        newAvatar();
        newNickField();
        newLevelLabel();
        newXpBar();
        newProgressLabel();
        newGamesPlayedLabel();
        newWinRateLabel();
        newDeleteButton();
    }

    private void arrangeElements() {
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

    private void newAvatarPicker() {
        avatarPicker = new AvatarPicker();
        avatarPicker.setVisible(false);
    }

    private void newInfoContainer() {
        infoContainer = new VBox();
    }

    private void newAvatar() {
        avatar = new Circle(20, 20, 20);
        avatar.setId("avatar");
        avatar.setOnMouseClicked(e -> avatarPicker.setVisible(!avatarPicker.isVisible()));
    }

    private void newNickField() {
        nickField = new TextField();
    }

    private void newLevelLabel() {
        levelLabel = new Label();
        levelLabel.setId("level-label");
    }

    private void newXpBar() {
        xpBar = new ProgressBar();
        xpBar.getStyleClass().add("xp-bar");
    }

    private void newProgressLabel() {
        progressLabel = new Label();
        progressLabel.setId("level-progress-label");
    }

    private void newGamesPlayedLabel() {
        gamesPlayedLabel = new Label();
        gamesPlayedLabel.setId("games-played-label");
    }

    private void newWinRateLabel() {
        winRateLabel = new Label();
        winRateLabel.setId("win-rate-label");
    }

    private void newDeleteButton() {
        deleteButton = new Button("Delete Account");
        deleteButton.setId("delete-button");
    }

    /**
     * Getters for the <code>AvatarPicker</code> of this, which can be opened by clicking in the avatar icon.
     * @return The <code>AvatarPicker</code> of this.
     */
    public AvatarPicker getAvatarPicker() {
        return avatarPicker;
    }

    /**
     * Set the action to perform when the user confirm the new text typed into the nickname prompt field.
     * @param action A consumer that takes as input the text typed and performs the desired action on it.
     */
    public void onNickType(Consumer<String> action) {
        nickField.setOnKeyReleased(e -> {
            if (e.getCode() != KeyCode.ENTER)
                return;

            action.accept(nickField.getText());
            nickField.clear();
            requestFocus(); // Used to remove focus from the text field
        });
    }

    /**
     * Set the action to perform when the delete button is clicked.
     * @param action Interface from JavaFX describing the mouse click event.
     */
    public void onDelete(EventHandler<MouseEvent> action) {
        deleteButton.setOnMouseClicked(action);
    }

    /**
     * Set the property of closing this when clicked outside.
     * @param container The pane that hold the click event which closes this.
     */
    // TODO fare che aggiunge l'azione al listener invece che sovrascriverla
    public void setCloseContainer(Pane container) {
        container.setOnMouseClicked(e -> {
            if (e.getPickResult().getIntersectedNode() != container)
                return;
            setVisible(false);
            avatarPicker.setVisible(false);
        });
    }

    @Override
    public void update(EventType event, HashMap<String, Object> data) {
        switch (event) {
            case INFO_RESET:
                nickField.setPromptText((String) data.get("nickname"));
                avatar.setFill(new ImagePattern(new Image((String) data.get("icon"))));
                xpBar.setProgress(0);
                progressLabel.setText("0%");
                levelLabel.setText("Level 1");
                gamesPlayedLabel.setText("Games: 0");
                winRateLabel.setText("Wins: 0");
                break;
            default:
                throwUnsupportedError(event, data);
        }
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
        }
    }

    @Override
    public void update(EventType event, int data) {
        switch (event) {
            case LEVELED_UP:
                levelLabel.setText("Level " + data);
                break;
            case USER_PLAYED_GAME:
                gamesPlayedLabel.setText("Games: " + data);
                break;
            case USER_WON:
                winRateLabel.setText("Wins: " + data);
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }

    @Override
    public void update(EventType event, double data) {
        switch (event) {
            case NEW_LEVEL_PROGRESS:
                xpBar.setProgress(data / 100);
                progressLabel.setText(data + "%");
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }
}
