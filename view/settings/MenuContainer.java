package view.settings;

import javafx.scene.layout.StackPane;

public class MenuContainer extends StackPane {
    protected SettingsMenu settings;
    protected ProfileMenu profile;

    protected MenuContainer () {
        setVisible(false);

        settings = new SettingsMenu();
        profile = new ProfileMenu();

        getChildren().addAll(settings, profile);
        
        setStyle("-fx-background-color: rgba(0, 0, 0, 0.7)");
    }

    protected void openSettings() {
        settings.setVisible(true);
        profile.setVisible(false);
    }

    protected void openProfile() {
        settings.setVisible(false);
        profile.setVisible(true);
    }
}
