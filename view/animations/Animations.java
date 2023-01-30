package view.animations;

import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public enum Animations {
    BLOCK_TURN,
    CARD_PLAYED,
    FOCUS_PLAYER,
    NEW_GAME,
    UNO_TEXT;

    private static final String ANIMATION_FOLDER = "resources/animations";

    public Animation get() {
        return new Animation(ANIMATION_FOLDER + "/" + this.toString());
    }

    /* NATIVE ANIMATIONS */

    public static final void resetTranslate(Node node) {
        TranslateTransition reset = new TranslateTransition(Duration.millis(300.0), node);
        reset.setByX(-node.getTranslateX());
        reset.setByY(-node.getTranslateY());
        reset.play();
    }

    private static boolean ongoing;

    /**
     * 
     * @param node The node to rotate.
     * @return If the animation started or not.
     */
    public static final boolean rotate360(Node node) {
        if (ongoing)
            return false;

        ongoing = true;
        RotateTransition transition = new RotateTransition();
        transition.setCycleCount(1);
        transition.setNode(node);
        transition.setDuration(Duration.seconds(1));
        transition.setFromAngle(0);
        transition.setToAngle(360);
        transition.setOnFinished(e -> ongoing = false);
        transition.play();
        return true;
    }

    public static final void zoomIn(Node node, double scalingFactor) {
        ScaleTransition zoomIn = new ScaleTransition(Duration.millis(100.0), node);

        if (node.getScaleX() <= 1.0 || node.getScaleY() <= 1.0) {
            zoomIn.setByX(1 + scalingFactor - node.getScaleX());
            zoomIn.setByY(1 + scalingFactor - node.getScaleY());
            zoomIn.play();
        }
    }

    public static final void zoomOut(Node node, double scalingFactor) {
        ScaleTransition zoomOut = new ScaleTransition(Duration.millis(100.0), node);

        if (node.getScaleX() != 1.0 || node.getScaleY() != 1.0) {
            zoomOut.setByX(1 - node.getScaleX());
            zoomOut.setByY(1 - node.getScaleY());
            zoomOut.play();
        }
    }
}
