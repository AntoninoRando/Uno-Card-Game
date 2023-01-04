package view.gameElements;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/* --- Mine ------------------------------- */

import events.toView.EventListener;
import events.toView.EventType;

/**
 * The GUI representation of a UNO card: it has an image and it is zoombale, but
 * it does not store card informations.
 */
public class CardContainer extends ImageView implements EventListener {
    /* --- Fields ----------------------------- */

    private static Path imgFolder = Paths.get("resources/AllUnoCards");

    private final ScaleTransition zoomIn = new ScaleTransition(Duration.millis(100.0), this);
    private final ScaleTransition zoomOut = new ScaleTransition(Duration.millis(100.0), this);

    public static HashMap<Integer, CardContainer> cards = new HashMap<>();

    /* --- Constructors ----------------------- */

    /**
     * Create a blank card with no image.
     */
    public CardContainer() {
        getStyleClass().add("card");
        setPreserveRatio(true);
        setFitWidth(150);
        makeZommable(0.5);
    }

    /**
     * Create a UNO card with the image associated to the given input card.
     * 
     * @param card The card info; used to detect which image to load, not to stores
     *             information about the card.
     */
    public CardContainer(int tag, String representation) {
        getStyleClass().add("card");
        loadImage(representation);
        setPreserveRatio(true);
        setFitWidth(150);
        makeZommable(0.5);
        cards.put(tag, this);
    }

    /* --- Body ------------------------------- */

    /**
     * Loads the image of the given input card from memory.
     * 
     * @param card The card info; used to detect which image to load.
     */
    private void loadImage(String representation) {
        Path imgPath = imgFolder.resolve(representation.concat(".png"));
        if (Files.notExists(imgPath))
            imgPath = imgFolder.resolve("MISSING.png");

        try {
            Image img = new Image(imgPath.toUri().toURL().toExternalForm());
            setImage(img);
        } catch (MalformedURLException e) {
        }
    }

    /**
     * Change the image according to the new input card.
     * 
     * @param card The card info; used to detect which image to load.
     */
    public void update(String representation) {
        loadImage(representation);
    }

    /**
     * Change all properties of this object to the properties of the given object.
     * 
     * @param card The card GUI object to copy.
     */
    public void update(CardContainer card) {
        // TODO finire di copiare anche gli altri campi
        setImage(card.getImage());
    }

    // TODO migliorare perche' e' un po' lagghi e ogni tanto si bugga
    private void makeZommable(Double scalingFactor) {
        setOnMouseEntered(e -> {
            if (getScaleX() <= 1.0 || getScaleY() <= 1.0) {
                // 1 + scalingFactor = getScaleX() + x
                zoomIn.setByX(1 + scalingFactor - getScaleX());
                zoomIn.setByY(1 + scalingFactor - getScaleY());
                zoomIn.play();
            }
        });

        setOnMouseExited(e -> {
            if (getScaleX() != 1.0 || getScaleY() != 1.0) {
                // 1 = getScaleX() - x
                zoomOut.setByX(1 - getScaleX());
                zoomOut.setByY(1 - getScaleY());
                zoomOut.play();
            }
        });
    }

    @Override
    public void update(EventType event, HashMap<String, Object> data) {
        switch (event) {
            case NEW_CARD:
                Platform.runLater(() -> new CardContainer((int) data.get("tag"), (String) data.get("representation")));
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }
}
