package view;

import javafx.scene.layout.Pane;

public class PlayzonePane extends Pane {
    /* SINGLETON */
    /* --------- */
    private static PlayzonePane instance;

    public static PlayzonePane getInstance() {
        if (instance == null)
            instance = new PlayzonePane();
        return instance;
    }

    private PlayzonePane() {
        getStyleClass().add("playzone");
        setMaxHeight(200);
        setMaxWidth(200);
    }
}
