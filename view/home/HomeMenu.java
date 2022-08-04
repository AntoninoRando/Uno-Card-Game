package view.home;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import view.settings.Settings;

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
        setAlignment(Pos.CENTER_LEFT);
        arrangeElements();
    }

    /* ---------------------------------------- */
    protected Node playButton;

    private Node createTitle() {
        Label title = new Label("JUno");
        title.getStyleClass().add("title");
        return title;
    }

    private Node createPlayButton() {
        playButton = new Button("Play");
        playButton.getStyleClass().add("button");
        return playButton;
    }

    private Node createAdventureButton() {
        Button adventure = new Button("Adventure");
        adventure.getStyleClass().add("button");
        return adventure;
    }

    private Node createProfileButton() {
        Button profile = new Button("Profile");
        profile.getStyleClass().add("button");

        profile.setOnMouseClicked(e -> Settings.openProfile());

        return profile;
    }

    private Node createExitButton() {
        Button exit = new Button("Exit");
        exit.getStyleClass().add("button");

        exit.setOnMouseClicked(e -> System.exit(0));

        return exit;
    }

    private void arrangeElements() {
        getChildren().addAll(createTitle(), createPlayButton(), createAdventureButton(), createProfileButton(), createExitButton());
    }
}
