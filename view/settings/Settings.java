package view.settings;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.function.Consumer;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public abstract class Settings {
    public static final MenuContainer MENU = new MenuContainer();

    /* SETTINGS */

    public static final Button SETTINGS_BUTTON = createSettingsButton();

    private static final Button createSettingsButton() {
        Button button = new Button();
        button.setId("settings-button");

        try {
            Image image = new Image(Paths.get("resources\\settingsIcon.png").toUri().toURL().toExternalForm());
            ImageView icon = new ImageView(image);
            icon.setFitWidth(50.0);
            icon.setFitHeight(50.0);
            button.setGraphic(icon);
        } catch (MalformedURLException e1) {
        }

        button.setOnMouseClicked(e -> {
            MENU.openSettings();
            MENU.setVisible(!MENU.isVisible());
        });

        return button;
    }

    public static void setRestartButtonAction(EventHandler<MouseEvent> action) {
        MENU.settings.restartButton.setOnMouseClicked(action);
    }

    public static void setQuitButtonAction(EventHandler<MouseEvent> action) {
        MENU.settings.quitButton.setOnMouseClicked(action);
    }

    /* PROFILE */

    public static final ProfileMenu PROFILE = new ProfileMenu();
    public static final AvatarPicker AVATAR_PICKER = new AvatarPicker();

    public static void setNickFieldAction(Consumer<String> action) {
        PROFILE.nickField.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                action.accept(PROFILE.nickField.getText());
                PROFILE.nickField.clear();
                PROFILE.requestFocus(); // Used to remove focus from the text field
                PROFILE.updateInfo();
            }
        });
    }

    public static void setDeleteAccountAction(EventHandler<MouseEvent> action) {
        PROFILE.deleteButton.setOnMouseClicked(e -> {
            action.handle(e);
            PROFILE.updateInfo();
        });
    }

    public static void setAvatarClickAction(EventHandler<MouseEvent> action) {
        PROFILE.avatar.setOnMouseClicked(action);
    }

    // TODO fare che aggiunge l'azione al listener invece che sovrascriverla
    public static void setCloseProfileOnClickOutside(Pane container) {
        container.setOnMouseClicked(e -> {
            if (e.getPickResult().getIntersectedNode() == container)
                PROFILE.setVisible(false);
        });
}
}
