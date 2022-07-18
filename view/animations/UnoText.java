package view.animations;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class UnoText {
    final static Path folderPath = Paths.get("resources\\AnimazioneUno+");
    final static File folder = new File("resources\\AnimazioneUno+");

    final static ImageView[] images = loadImages();

    public static ImageView[] loadImages() {
        ImageView[] images = new ImageView[folder.listFiles().length];

        for (int i = 0; i < images.length; i++) {
            try {
                Image img = new Image(folderPath.resolve(i + ".png").toUri().toURL().toExternalForm());
                images[i] = new ImageView(img);
                images[i].setPreserveRatio(true);
                images[i].setFitWidth(400.0);
            } catch (MalformedURLException e) {
            }
        }

        return images;
    }

    public static void play(Pane target) {
        Group animation = new Group(images[0]);

        Timeline t = new Timeline();
        t.setCycleCount(1);

        for (int i = 1; i < images.length; i++) {
            ImageView img = images[i];
            t.getKeyFrames()
                    .add(new KeyFrame(Duration.millis(100.0*i), e -> animation.getChildren().setAll(img)));
        }

        target.getChildren().add(animation); // TODO non so se poi vada tolto dal pane
        t.setOnFinished(e -> target.getChildren().remove(animation));
        t.play();
    }
}
