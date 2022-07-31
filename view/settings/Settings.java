package view.settings;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public abstract class Settings {
    public static final SettingsMenuContainer MENU = new SettingsMenuContainer();

    public static final Button BUTTON = createButton();

    private static final Button createButton() {
        Button button = new Button("Settings");
        button.setOnMouseClicked(e -> MENU.setVisible(!MENU.isVisible()));
        return button;
    }

    public static void setRestartButtonAction(EventHandler<MouseEvent> action) {
        MENU.menu.restartButton.setOnMouseClicked(action);
    }

    public static void setQuitButtonAction(EventHandler<MouseEvent> action) {
        MENU.menu.quitButton.setOnMouseClicked(action);
    }
}
