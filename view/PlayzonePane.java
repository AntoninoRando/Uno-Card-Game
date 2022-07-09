package view;

import javafx.scene.layout.StackPane;

public class PlayzonePane extends StackPane {
    public PlayzonePane() {
        getStyleClass().add("playzone");
        setMaxHeight(400);
        setMaxWidth(400);
    }
}
