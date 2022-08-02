package view.gameElements;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DeckContainer extends ImageView {
    /* SINGLETON PATTERN */
    /* ----------------- */
    private static DeckContainer instance;

    public static DeckContainer getInstance() {
        if (instance == null)
            instance = new DeckContainer();
        return instance;
    }

    private final Path imgPath = Paths.get("C:\\Users\\anton\\OneDrive\\Desktop\\AllUnoCards\\MISSING.png");

    public DeckContainer() {
        getStyleClass().add("deck");
        try {
            loadImage();
        } catch (MalformedURLException e) {
            throw new Error("Error while loading the deck image");
        }
    }

    private void loadImage() throws MalformedURLException {
        setImage(new Image(imgPath.toUri().toURL().toExternalForm()));
        setPreserveRatio(true);
        setFitWidth(150);
    }
}
