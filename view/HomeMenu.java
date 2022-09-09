package view;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class HomeMenu extends VBox {
    private static HomeMenu instance;

    public static HomeMenu getInstance() {
        if (instance == null)
            instance = new HomeMenu();
        return instance;
    }

    private HomeMenu() {
        getStyleClass().add("home-menu");
        initialize();
        arrangeElements();
    }
    
    private Label title;
    private Node playB;
    private Node profileB;
    private Node exitB;

    private void initialize() {
        newTitle();
        newPlayButton();
        newProfileButton();
        newExitButton();
    }

    private void arrangeElements() {
        setSpacing(10.0);
        setAlignment(Pos.CENTER_LEFT);
        getChildren().addAll(title, playB, profileB, exitB);
    }

    private void newTitle() {
        title = new Label("JUno");
        title.getStyleClass().add("title");
    }

    private void newPlayButton() {
        playB = new Button("Play");
        playB.getStyleClass().add("button");
    }

    private void newProfileButton() {
        profileB = new Button("Profile");
        profileB.getStyleClass().add("button");
    }

    private void newExitButton() {
        exitB = new Button("Exit");
        exitB.getStyleClass().add("button");
        exitB.setOnMouseClicked(e -> System.exit(0));
    }

    public void setPlayButtonAction(EventHandler<MouseEvent> action) {
        HomeMenu.getInstance().playB.setOnMouseClicked(action);
    }

    public void setProfileAction(EventHandler<MouseEvent> action) {
        HomeMenu.getInstance().profileB.setOnMouseClicked(action);
    }
}
