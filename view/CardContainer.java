package view;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import model.cards.Card;

public class CardContainer extends ImageView {
    // TODO non so se vanno bene per qualsiasi sistema operativo quei separatori
    private static Path imgFolder = Paths.get("C:\\Users\\anton\\OneDrive\\Desktop\\AllUnoCards");
    private Path imgPath;

    public CardContainer(Card card) {
        getStyleClass().add("card");
        imgPath = imgFolder.resolve(card.toString().concat(".png"));
        if (Files.notExists(imgPath))
            imgPath = imgFolder.resolve("MISSING.png");
        try {
            loadImage();
        } catch (MalformedURLException e) {
            throw new Error("Error while loading the image of: " + card.toString());
        }
    }

    private void loadImage() throws MalformedURLException {
        Image img = new Image(imgPath.toUri().toURL().toExternalForm());
        setImage(img);
        setPreserveRatio(true);
        setFitWidth(150);
        makeDraggable();
        makeZommable(0.5);
    }

    /* -------------------------------- */
    private double mouseAnchorX;
    private double mouseAnchorY;
    private static Optional<Bounds> dontResetZone = Optional.empty();

    public static void setDontResetZone(Bounds bounds) {
        dontResetZone = Optional.of(bounds);
    }

    private void makeDraggable() {
        setOnMousePressed(e -> {
            // When hovered the card is zoomed, so we reset the original size
            if (getScaleX() != 1.0 || getScaleY() != 1.0) {
                setScaleX(1.0);
                setScaleY(1.0);
            }
            mouseAnchorX = e.getSceneX() - getTranslateX();
            mouseAnchorY = e.getSceneY() - getTranslateY();
        });

        setOnMouseDragged(e -> {
            setTranslateX(e.getSceneX() - mouseAnchorX);
            setTranslateY(e.getSceneY() - mouseAnchorY);
        });

        setOnMouseReleased(e -> {
            if (dontResetZone.isPresent() && dontResetZone.get().contains(e.getSceneX(), e.getSceneY()))
                return;
            TranslateTransition reset = new TranslateTransition(Duration.millis(300.0));
            reset.setNode(this);
            reset.setByX(mouseAnchorX - e.getSceneX()); // setTranslateX(0);
            reset.setByY(mouseAnchorY - e.getSceneY()); // setTranslateY(0);
            reset.play();
        });
    }

    private final ScaleTransition zoomIn = new ScaleTransition(Duration.millis(100.0), this);
    private final ScaleTransition zoomOut = new ScaleTransition(Duration.millis(100.0), this);

    // TODO migliorare perche' e' un po' lagghi e ogni tanto si bugga
    private void makeZommable(Double scalingFactor) {
        setOnMouseEntered(e -> {
            if (getScaleX() <= 1.0 || getScaleY() <= 1.0) {
                // 1 + scalingFactor  = getScaleX() + x
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
}
