package view.settings;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * A GUI element containing different settings about the applications that are
 * changeable by the user.
 */
public class SettingsMenu extends BorderPane {
    private static SettingsMenu instance;

    public static SettingsMenu getInstance() {
        if (instance == null)
            instance = new SettingsMenu();
        return instance;
    }

    private SettingsMenu() {
        getStyleClass().add("settings-menu");
        initialize();
        arrangeElements();
    }

    private Label title;
    private VBox optionsMenu;
    private HBox contextMenu;
    private Button general;
    private Button audio;

    private void initialize() {
        newTitle();
        newOptionsMenu();
        newContextMenu();
    }

    private void arrangeElements() {
        setMaxHeight(500.0);
        setMaxWidth(700.0);
        setPrefWidth(700.0);
        setPrefHeight(500.0);

        setTop(title);
        setLeft(optionsMenu);
        setBottom(contextMenu);
    }

    private void newTitle() {
        title = new Label("Settings");
        title.getStyleClass().add("label");
    }

    private void newOptionsMenu() {
        general = new Button("General");
        audio = new Button("Audio");
        optionsMenu = new VBox(general, audio);

        general.getStyleClass().add("button");
        audio.getStyleClass().add("button");
        optionsMenu.getStyleClass().add("VBox");
    }

    private void newContextMenu() {
        contextMenu = new HBox();
        contextMenu.setAlignment(Pos.CENTER);
    }

    /**
     * Set the nodes that will appear only during a game.
     * 
     * @param nodes The nodes with their properties.
     */
    public void addOptions(Node... nodes) {
        contextMenu.getChildren().setAll(nodes);
    }

    public void removeOptions() {
        contextMenu.getChildren().clear();
    }
}
