package view.settings;

import javafx.scene.control.Button;

public abstract class Settings {
    public static final SettingsMenuContainer MENU = new SettingsMenuContainer();

    public static final Button BUTTON = createButton();

    private static final Button createButton() {
        Button button = new Button("Settings");
        button.setOnMouseClicked(e -> {
            if (MENU.isVisible())
                MENU.setVisible(false);
            else
                MENU.setVisible(true);
        });
        return button;
    }
}
