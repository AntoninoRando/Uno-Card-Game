package view.gameElements;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.animation.ScaleTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import prefabs.Card;

public class CardContainer extends ImageView {
    private static Path imgFolder = Paths.get("resources/AllUnoCards");

    public CardContainer() {
        getStyleClass().add("card");
        setPreserveRatio(true);
        setFitWidth(150);
        makeZommable(0.5);
    }
    
    public CardContainer(Card card) {
        getStyleClass().add("card");
        loadImage(card);
        setPreserveRatio(true);
        setFitWidth(150);
        makeZommable(0.5);
    }

    private void loadImage(Card card) {
        Path imgPath = imgFolder.resolve(card.toString().concat(".png"));
        if (Files.notExists(imgPath))
            imgPath = imgFolder.resolve("MISSING.png");

        try {
            Image img = new Image(imgPath.toUri().toURL().toExternalForm());
            setImage(img);
        } catch (MalformedURLException e) {
        }
    }

    public void update(Card card) {
        loadImage(card);
    }

    public void update(CardContainer card) {
        // TODO finire di copiare anche gli altri campi
        setImage(card.getImage());
    }

    /* -------------------------------- */
    private final ScaleTransition zoomIn = new ScaleTransition(Duration.millis(100.0), this);
    private final ScaleTransition zoomOut = new ScaleTransition(Duration.millis(100.0), this);

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
}
