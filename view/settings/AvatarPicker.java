package view.settings;

import java.util.HashMap;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import model.data.UserInfo;

// TODO change to GridPane
public class AvatarPicker extends StackPane {
    private ScrollPane container = new ScrollPane();
    private HashMap<Circle, String> icons = fillIcons();
    private VBox grid = new VBox();
    private int rowCapacity = 5;
    private Button closeButton = createCloseButton();

    AvatarPicker() {
        setMaxHeight(400.0);
        setMaxWidth(400.0);
        setPrefWidth(400.0);
        setPrefHeight(400.0);

        setId("avatar-picker");

        arrangeElements();
    }

    private Button createCloseButton() {
        Button close = new Button("X");
        close.getStyleClass().add("button");
        close.setOnMouseClicked(e -> this.setVisible(false));
        return close;
    }

    private HashMap<Circle, String> fillIcons() {
        HashMap<Circle, String> map = new HashMap<>();
        // TODO fare che scorre la directory
        map.put(createIcon("resources\\icons\\blood.png"), "resources\\icons\\blood.png");
        map.put(createIcon("resources\\icons\\night.png"), "resources\\icons\\night.png");
        map.put(createIcon("resources\\icons\\queen.png"), "resources\\icons\\queen.png");
        map.put(createIcon("resources\\icons\\tree.png"), "resources\\icons\\tree.png");
        map.put(createIcon("resources\\icons\\pirate.png"), "resources\\icons\\pirate.png");
        map.put(createIcon("resources\\icons\\alien.png"), "resources\\icons\\alien.png");
        map.put(createIcon("resources\\icons\\Dragonfly dst.png"), "resources\\icons\\Dragonfly dst.png");
        return map;
    }

    private Circle createIcon(String pathname) {
        Circle avatar = new Circle(30, new ImagePattern(new Image(pathname)));
        avatar.setOnMouseClicked(e -> {
            UserInfo.setIconPath(pathname);
            Settings.PROFILE.updateInfo();
        });
        return avatar;
    }

    private void arrangeElements() {
        grid.setSpacing(20.0);

        HBox row = new HBox();
        row.setSpacing(20.0);
        grid.getChildren().add(row);

        int columnN = 0;
        for (Circle icon : icons.keySet()) {
            row.getChildren().add(icon);
            columnN++;
            if (columnN == rowCapacity) {
                row = new HBox();
                row.setSpacing(20.0);
                grid.getChildren().add(row);
            }
        }

        container.setContent(grid);
        getChildren().addAll(container, closeButton);
        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);
    }
}
