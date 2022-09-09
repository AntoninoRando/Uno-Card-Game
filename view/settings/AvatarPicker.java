package view.settings;

import java.util.Collection;
import java.util.function.Consumer;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

/**
 * A pane displaying in grid clickable icons. 
 */
public class AvatarPicker extends StackPane {
    private GridPane grid;
    private final int iconsPerLine = 5;
    private ScrollPane container;
    private Button closeButton;
    private Consumer<String> choiceAction;

    public AvatarPicker() {
        initialize();
        arrangeElements();
    }

    private void initialize() {
        grid = new GridPane();
        container = new ScrollPane(grid);
        closeButton = closeButton();
    }

    private void arrangeElements() {
        setId("avatar-picker");
        setMaxHeight(400.0);
        setMaxWidth(400.0);
        setPrefWidth(400.0);
        setPrefHeight(400.0);

        getChildren().addAll(container, closeButton);
        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);
    }

    /**
     * Set the action to perform when an icon is clicked.
     * @param action A consumer that takes as input the icon path and performs the desired action on it.
     */
    public void onChoice(Consumer<String> action) {
        choiceAction = action;
    }

    /**
     * Set all the clickable icons.
     * @param iconsPaths A collection of all the icons paths.
     */
    public void addOptions(Collection<String> iconsPaths) {
        String[] paths = iconsPaths.toArray(String[]::new);

        for (int i = 0; i < paths.length; i++) {
            String path = paths[i];
            Circle icon = createIcon(path);
            icon.setOnMouseClicked(e -> choiceAction.accept(path));
            GridPane.setColumnIndex(icon, i % iconsPerLine);
            GridPane.setRowIndex(icon, i / iconsPerLine);
            grid.getChildren().add(icon);
        }
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
}
