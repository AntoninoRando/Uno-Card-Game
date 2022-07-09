package view;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.cards.Card;

public class CardContainer {
    private Card card;
    private static Path imgFolder = Paths.get("C:\\Users\\anton\\OneDrive\\Desktop\\AllUnoCards"); // TODO non so se vanno bene per qualsiasi sistema operativo quei separatori
    private Path imgPath;
    private ImageView node;
    private double scaleX = 0.3;
    private double scaleY = 0.3;

    public CardContainer(Card card) {
        this.card = card;
        loadImage();
    }

    private void loadImage() {
        imgPath = imgFolder.resolve(card.toString().concat(".png"));

        try {
            Image img = new Image(imgPath.toUri().toURL().toExternalForm());
            node = new ImageView(img);
            node.setScaleX(scaleX);
            node.setScaleY(scaleY);
            new Draggable().makeDraggable(node);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public ImageView getNode() {
        return node;
    }
}
