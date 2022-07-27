package view;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HomeMenu extends VBox {
        /* SINGLETON */
    /* --------- */
    private static HomeMenu instance;

    public static HomeMenu getInstance() {
        if (instance == null)
            instance = new HomeMenu();
        return instance;
    }

    private HomeMenu() {
        getStyleClass().add("home-menu");
        setSpacing(10.0);

        Label title = new Label("JUno");
        title.getStyleClass().add("title");
        getChildren().add(title);
    }

    /* ---------------------------------------- */
}
