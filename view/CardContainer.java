package view;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    }

    /* -------------------------------- */
    private double mouseAnchorX;
    private double mouseAnchorY;

    public void makeDraggable() {
        setOnMousePressed(e -> {
            mouseAnchorX = e.getSceneX() - getTranslateX();
            mouseAnchorY = e.getSceneY() - getTranslateY();
        });

        setOnMouseDragged(e -> {
            setTranslateX(e.getSceneX() - mouseAnchorX);
            setTranslateY(e.getSceneY() - mouseAnchorY);
        });

        setOnMouseReleased(e -> {
            // TODO clear changes if the card wasn't released in the play zone
            setTranslateX(0);
            setTranslateY(0);
        });
    }
}
