package view.settings;

import java.util.Collection;
import java.util.HashMap;
import java.util.function.Consumer;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class AvatarPicker extends StackPane {
    private ScrollPane container = new ScrollPane();
    private HashMap<Circle, String> icons = new HashMap<>();
    private VBox grid = new VBox();
    private int rowCapacity = 5;
    private Button closeButton = closeButton();
    private Consumer<String> choiceAction;

    AvatarPicker() {
        stylize();
    }

    public void onChoice(Consumer<String> action) {
        choiceAction = action;
    }

    public void addOptions(Collection<String> iconsPaths) {
        iconsPaths.forEach(icoPath -> icons.put(createIcon(icoPath), icoPath));
        arrangeElements();
    }

    private Button closeButton() {
        Button close = new Button("X");
        close.getStyleClass().add("button");
        close.setOnMouseClicked(e -> this.setVisible(false));
        return close;
    }

    private Circle createIcon(String iconPath) {
        Circle avatar = new Circle(30, new ImagePattern(new Image(iconPath)));
        return avatar;
    }

    private void arrangeElements() {
        grid.setSpacing(20.0);

        HBox row = new HBox();
        row.setSpacing(20.0);
        grid.getChildren().add(row);

        int columnN = 0;
        for (Circle icon : icons.keySet()) {
            icon.setOnMouseClicked(e -> choiceAction.accept(icons.get(icon)));
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

    private void stylize() {
        setId("avatar-picker");
        setMaxHeight(400.0);
        setMaxWidth(400.0);
        setPrefWidth(400.0);
        setPrefHeight(400.0);
    }
}
