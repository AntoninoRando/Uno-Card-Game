package view.settings;

import javafx.scene.layout.StackPane;

public class MenuContainer extends StackPane {
    SettingsMenu settings;
    ProfileMenu profile;
    AvatarPicker avatarPicker;

    MenuContainer() {
        setVisible(false);

        settings = new SettingsMenu();
        profile = new ProfileMenu();
        avatarPicker = new AvatarPicker();
        avatarPicker.setVisible(false);

        getChildren().addAll(settings, profile, avatarPicker);

        setStyle("-fx-background-color: rgba(0, 0, 0, 0.7)");

        // Close the menu when user click outside
        setOnMouseClicked(e -> {
            if (e.getPickResult().getIntersectedNode() == this)
                setVisible(false);
        });
    }

    void openSettings() {
        settings.setVisible(true);
        profile.setVisible(false);
        avatarPicker.setVisible(false);
    }

    void openProfile() {
        settings.setVisible(false);
        profile.setVisible(true);
        avatarPicker.setVisible(false);
    }
}
