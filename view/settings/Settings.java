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

public abstract class Settings {
    public static final MenuContainer MENU = new MenuContainer();

    /* SETTINGS */
    public static final Button SETTINGS_BUTTON = createSettingsButton();

    private static final Button createSettingsButton() {
        Button button = new Button();
        button.setId("settings-button");

        try {
            Image image = new Image(Paths.get("resources\\settingsIcon.png").toUri().toURL().toExternalForm());
            button.setGraphic(new ImageView(image));
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
    public static void setNickFieldAction(Consumer<String> action) {
        MENU.profile.nickField.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                action.accept(MENU.profile.nickField.getText());
                MENU.profile.nickField.clear();
                MENU.requestFocus(); // Used to remove focus from the text field
                MENU.profile.updateInfo();
            }
        });
    }

    public static void setDeleteAccountAction(EventHandler<MouseEvent> action) {
        MENU.profile.deleteButton.setOnMouseClicked(e -> {
            action.handle(e);
            MENU.profile.updateInfo();
        });
    }

    public static void openProfile() {
        MENU.profile.updateInfo();
        MENU.openProfile();
        MENU.setVisible(!MENU.isVisible());
    }
}
