package view.animations;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * The class represents an animation. It automatically loads and stores the
 * single images which are the frames of the animation: just give the path to
 * the folder gathering them all (each images must be named "0.png", "1.png",
 * "2.png, ... whether they are the first, second, third, ... frame in the
 * animation).
 * <hr>
 * The method <code>play</code> starts the animation.
 */
public class Animation {
    private static Path folderPath;
    private static File folder;
    protected ImageView[] images;

    protected Animation(String folderPathname) {
        folderPath = Paths.get(folderPathname);
        folder = new File(folderPathname);
        images = loadImages();
    }

    private final static ImageView[] loadImages() {
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

    public void play() {
        playAndThen(e -> {});
    }

    public void playAndThen(EventHandler<ActionEvent> action) {
        Group animation = new Group(images[0]);

        Timeline t = new Timeline();
        t.setCycleCount(1);

        for (int i = 1; i < images.length; i++) {
            ImageView img = images[i];
            KeyFrame frame = new KeyFrame(Duration.millis(60.0 * i), e -> animation.getChildren().setAll(img));
            t.getKeyFrames().add(frame);
        }

        t.setOnFinished(e -> {
            action.handle(e);
            AnimationLayer.getInstance().removeAnimationGroup(animation);
        });
        AnimationLayer.getInstance().addAnimationGroup(animation);
        t.play();
    }

    public void load() {
        // Once invoked, the class constructor, that loads the images, will certainly
        // have been called (before or by this method call).
    };
}
