package view.gameElements;

import javafx.scene.layout.Pane;

public class PlayzonePane extends Pane {
    /* --- Singleton -------------------------- */
    
    private static PlayzonePane instance;

    public static PlayzonePane getInstance() {
        if (instance == null)
            instance = new PlayzonePane();
        return instance;
    }

    private PlayzonePane() {
        getStyleClass().add("playzone");
        setMaxHeight(400.0);
        setMaxWidth(400.0);
        setTranslateY(-130.0);
    }
}
