package controller.behaviors;

import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

/**
 * Through this control a card can be dragged, and if it drops into a target,
 * the <code>InputListener</code> will be notified.
 */
public class DragAndDrop extends Behavior<MouseEvent> {
    /* --- Fields ----------------------------- */

    protected Node source;
    protected Node target;
    protected double mouseAnchorX;
    protected double mouseAnchorY;
    protected boolean state; // true if the source has been dragged over the target

    /* ---.--- Getters and Setters ------------ */

    public boolean getState() {
        return state;
    }

    public void setState(boolean value) {
        state = value;
    }

    /* --- Constructors ----------------------- */

    public DragAndDrop(Node source, Node target) {
        this.source = source;
        this.target = target;
        applyBehavior();
    }

    /* --- Body ------------------------------- */

    @Override
    public boolean behave(MouseEvent e) {
        if (e.getButton().equals(MouseButton.SECONDARY))
            return false;

        if (e.getPickResult().getIntersectedNode() != target) {
            animateReset(e);
            return false;
        }

        animateDrop();
        setState(true);
        return true;
    }

    protected void applyBehavior() {
        source.setOnMousePressed(this::dragStart);
        source.setOnMouseDragged(this::dragRunning);
        source.setOnMouseReleased(this::onEnd);
    }

    protected void dragStart(MouseEvent e) {
        // When we drag we want the Node to be in its original size
        if (source.getScaleX() != 1.0 || source.getScaleY() != 1.0) {
            source.setScaleX(1.0);
            source.setScaleY(1.0);
        }

        mouseAnchorX = e.getSceneX() - source.getTranslateX();
        mouseAnchorY = e.getSceneY() - source.getTranslateY();
    }

    protected void dragRunning(MouseEvent e) {
        source.setTranslateX(e.getSceneX() - mouseAnchorX);
        source.setTranslateY(e.getSceneY() - mouseAnchorY);
    }

    // TODO rendere le animazioni altre classi, magarid decorators

    /**
     * Plays an animation when the drag source is dropped on the target. After that
     * animation, an action is performed.
     * 
     * @param source   The drag source.
     * @param onFinish The action to perform after the animation.
     */
    public void animateDrop() {
        ScaleTransition zoom = new ScaleTransition(Duration.millis(150.0), source);
        zoom.setByX(0.3);
        zoom.setByY(0.3);
        ScaleTransition zoomOut = new ScaleTransition(Duration.millis(150.0), source);
        zoomOut.setByX(-0.3);
        zoomOut.setByY(-0.3);
        zoomOut.setDelay(Duration.millis(100.0));

        zoom.setOnFinished(e -> zoomOut.play());

        RotateTransition alignToTarget = new RotateTransition(Duration.millis(300.0), source);
        alignToTarget.setByAngle(target.getRotate() - source.getRotate());

        TranslateTransition moveToCenter = new TranslateTransition(Duration.millis(100.0), source);
        double xSource = source.localToScene(source.getBoundsInLocal()).getCenterX();
        double ySource = source.localToScene(source.getBoundsInLocal()).getCenterY();
        double xTarget = target.localToScene(target.getBoundsInLocal()).getCenterX();
        double yTarget = target.localToScene(target.getBoundsInLocal()).getCenterY();
        moveToCenter.setByX(xTarget - xSource); // xSource + translate = xPz -> translate = xPz-xSource
        moveToCenter.setByY(yTarget - ySource);
        moveToCenter.setOnFinished(e -> {
            alignToTarget.play();
            zoom.play();
        });
        moveToCenter.play();
    }

    public void animateReset(MouseEvent e) {
        TranslateTransition reset = new TranslateTransition(Duration.millis(300.0), source);
        reset.setByX(mouseAnchorX - e.getSceneX()); // correspond to setTranslateX(0);
        reset.setByY(mouseAnchorY - e.getSceneY()); // correspond to setTranslateY(0);
        reset.play();
    }

    public void reset() {
        // TODO fare che ripristina la transition, così che se il drag è andato a buon
        // fine ma la carta non poteva essere giocata, basta chiamare questo metodo.
    }
}