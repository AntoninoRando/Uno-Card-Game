package view.home;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public abstract class Homes {
    public static void setPlayButtonAction(EventHandler<MouseEvent> action) {
        HomeMenu.getInstance().playButton.setOnMouseClicked(action);
    }

    public static void setProfileAction(EventHandler<MouseEvent> action) {
        HomeMenu.getInstance().profile.setOnMouseClicked(action);
    }
}
