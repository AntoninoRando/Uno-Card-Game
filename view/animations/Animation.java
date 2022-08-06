package view.animations;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

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
    private Path folderPath;
    private File folder;
    private ImageView[] images;
    private double frameDuration = 60.0;
    private EventHandler<ActionEvent> onFinishAction = e -> {
        AnimationLayer.getInstance().setVisible(false);
    };

    public Animation(String folderPathname) {
        folderPath = Paths.get(folderPathname);
        folder = new File(folderPathname);
        images = loadImages();
    }

    public void setFrameDuration(double duration) {
        frameDuration = duration;
    }

    public void setOnFinishAction(EventHandler<ActionEvent> action) {
        onFinishAction = e -> {
            action.handle(e);
            AnimationLayer.getInstance().setVisible(false);
        };
    }

    private final ImageView[] loadImages() {
        if (Animations.imagesLoaded.containsKey(folderPath.toString()))
            return Animations.imagesLoaded.get(folderPath.toString());

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

    public void play(Group animationLayer) {
        AnimationLayer.getInstance().setVisible(true);
        Group animation = new Group(images[0]);

        Timeline t = new Timeline();
        t.setCycleCount(1);

        for (int i = 1; i < images.length; i++) {
            ImageView img = images[i];
            KeyFrame frame = new KeyFrame(Duration.millis(frameDuration * i), e -> animation.getChildren().setAll(img));
            t.getKeyFrames().add(frame);
        }

        t.setOnFinished(e -> {
            onFinishAction.handle(e);
            animationLayer.getChildren().remove(animation);
        });
        animationLayer.getChildren().add(animation);
        t.play();
    }

    private static LinkedList<Animation> queue = new LinkedList<>();
    private static boolean isPlaying;

    public void playOnQueue(Group animationLayer) {
        queue.offer(this);
        if (!isPlaying)
            playQueue(animationLayer);
    }

    public void playQueue(Group animationLayer) {
        if (queue.isEmpty())
            return;
        
        AnimationLayer.getInstance().setVisible(true);

        isPlaying = true;
        Animation currentAnimation = queue.removeFirst();
        EventHandler<ActionEvent> action = currentAnimation.onFinishAction;

        currentAnimation.setOnFinishAction(e -> {
            action.handle(e);
            isPlaying = false;
            playQueue(animationLayer);
        });
        currentAnimation.play(animationLayer);
    }

    public void load() {
        Animations.imagesLoaded.put(folderPath.toString(), images);
    };

    /* ----------------------------------------------- */

    public static CountDownLatch latch = new CountDownLatch(1);

    public void playAndWait(Group animationLayer) {
        AnimationLayer.getInstance().setVisible(true);
        Group animation = new Group(images[0]);

        Timeline t = new Timeline();
        t.setCycleCount(1);

        for (int i = 1; i < images.length; i++) {
            ImageView img = images[i];
            KeyFrame frame = new KeyFrame(Duration.millis(frameDuration * i), e -> animation.getChildren().setAll(img));
            t.getKeyFrames().add(frame);
        }

        t.setOnFinished(e -> {
            onFinishAction.handle(e);
            animationLayer.getChildren().remove(animation);
            latch.countDown();
            latch = new CountDownLatch(1);
        });
        animationLayer.getChildren().add(animation);
        t.play();
    }
}
