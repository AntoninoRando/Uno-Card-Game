package view.settings;

import javafx.scene.layout.StackPane;

public class MenuContainer extends StackPane {
    SettingsMenu settings = new SettingsMenu();

    MenuContainer() {
        setVisible(false);
        getChildren().add(settings);

        setStyle("-fx-background-color: rgba(0, 0, 0, 0.7)");

        // Close the menu when user click outside
        setOnMouseClicked(e -> {
            if (e.getPickResult().getIntersectedNode() == this)
                setVisible(false);
        });
    }

    void openSettings() {
        settings.setVisible(true);
    }
}
