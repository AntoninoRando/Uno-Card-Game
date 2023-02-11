package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Implements the <em>Flyweight</em> pattern as <b>Flyweight</b>.
 * <p>
 * A unique image that can be applied to multiple containers.
 */
public class Sprite {
    private Image image;
    private String name;

    /**
     * Loads the image file in input.
     * 
     * @param filePath The file path.
     */
    public Sprite(String filePath, String name) {
        image = new Image(filePath);
        this.name = name;
    }

    
    /** 
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Implements the <em>Flyweight</em> pattern as <b>operation</b>.
     * <p>
     * Applies this image to a container.
     * 
     * @param width     The width of the image (ratio will be preserved).
     * @param container The container of the image.
     */
    public void draw(double width, ImageView container) {
        container.setPreserveRatio(true);
        container.setFitWidth(width);
        container.setImage(image);
    }
}
