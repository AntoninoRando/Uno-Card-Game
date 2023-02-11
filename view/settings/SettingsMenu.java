package view.settings;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import view.GUIContainer;

/**
 * A GUI element containing different settings about the applications that are
 * changeable by the user.
 */
public class SettingsMenu extends BorderPane implements GUIContainer {
    private static SettingsMenu instance;

    
    /** 
     * @return SettingsMenu
     */
    public static SettingsMenu getInstance() {
        if (instance == null)
            instance = new SettingsMenu();
        return instance;
    }

    private SettingsMenu() {
        initialize();
    }

    /* --- Fields ----------------------------- */

    private Label title;
    private VBox optionsMenu;
    private HBox contextMenu;
    private Button general;
    private Button audio;

    /* --- Body ------------------------------- */

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

    /* --- GUIContainer ----------------------- */

    @Override
    public void createElements() {
        title = new Label("Settings");
        general = new Button("General");
        audio = new Button("Audio");
        optionsMenu = new VBox(general, audio);
        contextMenu = new HBox();
    }

    @Override
    public void arrangeElements() {
        getStyleClass().add("settings-menu");
        setMaxHeight(500.0);
        setMaxWidth(700.0);
        setPrefWidth(700.0);
        setPrefHeight(500.0);

        setTop(title);
        setLeft(optionsMenu);
        setBottom(contextMenu);

        title.getStyleClass().add("label");
        general.getStyleClass().add("button");
        audio.getStyleClass().add("button");
        optionsMenu.getStyleClass().add("VBox");

        contextMenu.setAlignment(Pos.CENTER);
    }

    @Override
    public void applyBehaviors() {
    }
}
