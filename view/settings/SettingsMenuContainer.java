package view.settings;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class SettingsMenuContainer extends HBox {
    public SettingsMenuContainer () {
        setVisible(false);
        SettingsMenu menu = new SettingsMenu();
        getChildren().add(menu);
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: rgba(0, 0, 0, 0.7)");
    }
}
