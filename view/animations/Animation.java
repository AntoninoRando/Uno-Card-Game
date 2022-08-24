package view.animations;

import java.io.File;

import java.net.MalformedURLException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.util.Pair;

public class Animation {
    private Path folderPath;
    private File folder;
    private double frameDuration = 60.0;
    private int startFrame;
    private int stopFrame;
    private EventHandler<ActionEvent> onFinishAction = e -> {
    };
    private Optional<Double> resizeW = Optional.empty();
    private Optional<Double> resizeH = Optional.empty();
    private Optional<Double> sceneX = Optional.empty();
    private Optional<Double> sceneY = Optional.empty();
    public CountDownLatch latch = new CountDownLatch(1);
    private boolean willCountdown, willStay;

    private static HashMap<String, ImageView[]> imagesLoaded = new HashMap<String, ImageView[]>();

    public Animation(String folderPathname) {
        folderPath = Paths.get(folderPathname);
        folder = new File(folderPathname);
        load();
    }

    /* GETTERS AND SETTERS */

    public void setFrameDuration(double frameDuration) {
        this.frameDuration = frameDuration;
    }

    public void setOnFinishAction(EventHandler<ActionEvent> onFinishAction) {
        this.onFinishAction = onFinishAction;
    }

    public void setDimensions(Double width, Double height) {
        if (width != null)
            resizeW = Optional.of(width);
        if (height != null)
            resizeH = Optional.of(height);
    }

    public void setSceneCoordinates(Double x, Double y) {
        if (x != null)
            sceneX = Optional.of(x);
        if (y != null)
            sceneY = Optional.of(y);
    }

    public void setWillCountdown(boolean value) {
        willCountdown = value;
    }

    public void setWillStay(boolean value) {
        willStay = value;
    }

    public void setStartFrame(int frame) {
        startFrame = frame;
    }

    public void setStopFrame(int frame) {
        stopFrame = frame;
    }

    /* LOADING METHODS */

    public void load() {
        ImageView[] images = imagesLoaded.get(folderPath.toString());

        stopFrame = folder.listFiles().length - 1;

        if (images != null) 
            return;

        images = new ImageView[stopFrame + 1];

        try {
            for (int i = 0; i <= stopFrame; i++)
                images[i] = new ImageView(new Image(folderPath.resolve(i + ".png").toUri().toURL().toExternalForm()));
        } catch (MalformedURLException e) {
        }

        imagesLoaded.put(folderPath.toString(), images);
    };

    public Pair<Timeline, AnchorPane> createAnimation(Consumer<AnchorPane> action) {
        ImageView[] images = imagesLoaded.get(folderPath.toString());

        Timeline t = new Timeline();
        t.setCycleCount(1);

        AnchorPane animation = new AnchorPane();
        // Since the animation layer is a StackPane, if this field were false we could
        // not click any node behind the StackPane
        animation.setMouseTransparent(true);

        for (int i = startFrame; i <= stopFrame; i++) {
            ImageView img = images[i];

            if (resizeW.isEmpty() || resizeH.isEmpty())
                img.setPreserveRatio(true);
            resizeW.ifPresent(w -> img.setFitWidth(w));
            resizeH.ifPresent(h -> img.setFitHeight(h));

            img.setVisible(false);
            int j = i - 1;
            animation.getChildren().add(img);
            KeyFrame frame = new KeyFrame(Duration.millis(frameDuration * i), e -> {
                if (j >= 0)
                    images[j].setVisible(false);
                img.setVisible(true);
            });

            sceneX.ifPresent(x -> AnchorPane.setLeftAnchor(img, x)); // x = animationRealX + translateQuantity
            sceneY.ifPresent(y -> AnchorPane.setTopAnchor(img, y));

            t.getKeyFrames().add(frame);
        }

        t.setOnFinished(e -> {
            onFinishAction.handle(e);
            action.accept(animation);
        });

        return new Pair<Timeline, AnchorPane>(t, animation);
    }

    private Pair<Timeline, AnchorPane> play(Pane animationLayer, Consumer<AnchorPane> action, int index) {
        Pair<Timeline, AnchorPane> animation = createAnimation(action);

        animationLayer.getChildren().add(index, animation.getValue());

        // TODO anche la soluzione sotto dovrebbe andar bene, al posto di settare l'anchor per ogni immagine
        // Node firstFrame = animation.getValue().getChildren().get(0);
        // Bounds b = firstFrame.localToScene(firstFrame.getBoundsInLocal());
        // sceneX.ifPresent(x -> animation.getValue().setTranslateX(x - b.getMinX())); // x = animationRealX +
        //                                                                             // translateQuantity
        // sceneY.ifPresent(y -> animation.getValue().setTranslateY(y - b.getMinY()));

        animation.getKey().play();

        return animation;
    }

    public Pair<Timeline, AnchorPane> play(Pane animationLayer) {
        return play(animationLayer, g -> {
            if (!willStay)
                animationLayer.getChildren().remove(g);
            if (willCountdown)
                latch.countDown();
        }, animationLayer.getChildren().size());
    }

    public Pair<Timeline, AnchorPane> play(Pane animationLayer, int index) {
        return play(animationLayer, g -> {
            if (!willStay)
                animationLayer.getChildren().remove(g);
            if (willCountdown)
                latch.countDown();
        }, index);
    }

    public void resetLatch() {
        latch = new CountDownLatch(1);
    }
}
