package view.settings;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.gameLogic.Loop;
import model.events.EventListener;

public class SettingsMenu extends BorderPane implements EventListener {
    protected Node restartButton = createRestartButton();
    protected Node quitButton = createQuitButton();

    public SettingsMenu() {
        setMaxHeight(500.0);
        setMaxWidth(700.0);
        setPrefWidth(700.0);
        setPrefHeight(500.0);

        arrangeElements();
        getStyleClass().add("settings-menu");

        Loop.events.subscribe(this, "gameStart");
    }

    private Node createTitle() {
        Label text = new Label("Settings");
        text.getStyleClass().add("label");
        return text;
    }

    private Node createOptions() {
        VBox options = new VBox();

        Button general = new Button("General");
        Button audio = new Button("Audio");

        general.getStyleClass().add("button");
        audio.getStyleClass().add("button");
        options.getStyleClass().add("VBox");

        options.getChildren().addAll(general, audio);
        return options;
    }

    private Node createContextMenu() {
        HBox contextOptions = new HBox();
        contextOptions.setAlignment(Pos.CENTER);
        return contextOptions;
    }

    private Node createRestartButton() {
        Button restartButton = new Button("Restart");
        return restartButton;
    }

    private Node createQuitButton() {
        Button quitButton = new Button("Quit");
        return quitButton;
    }

    private void arrangeElements() {
        setTop(createTitle());
        setLeft(createOptions());
        setBottom(createContextMenu());
    }

    /* EVENT LISTENER */

    @Override
    public void update(String eventType, Object data) {
    }

    @Override
    public void update(String eventType, Object... data) {
        switch (eventType) {
            case "gameStart":
                Platform.runLater(
                        () -> ((HBox) getBottom()).getChildren().addAll(restartButton, quitButton));
                break;
        }
    }
}
