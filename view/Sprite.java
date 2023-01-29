package view;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Implements the <em>Flyweight</em> pattern as <b>Flyweight</b>.
 */
public class Sprite {
    private Image image;

    /**
     * Loads the image associated with the given string.
     */
    public Sprite(String filePath) {
        Path file = Paths.get(filePath);

        if (Files.notExists(file))
            throw new Error("File named " + filePath + " doesn't exist.");

        try {
            image = new Image(file.toUri().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    // operation
    public void draw(double width, ImageView container) {
        container.setPreserveRatio(true);
        container.setFitWidth(width);
        container.setImage(image);
    }
}
