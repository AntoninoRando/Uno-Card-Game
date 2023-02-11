package controller.behaviors;

import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

/**
 * Mouse drag and drop event. It exhibites when the user clicks on the source
 * <em>and</em> releases the click only when the source is over a given target.
 */
public class DragAndDrop extends Behavior<MouseEvent> {
    /* --- Fields ----------------------------- */

    protected Node dropTarget;
    protected double mouseAnchorX;
    protected double mouseAnchorY;
    /**
     * <code>true</code> only if the source has been dragged on the target.
     */
    protected boolean state;

    /* --- Constructors ----------------------- */

    public DragAndDrop(Node source, Node target) {
        this.source = source;
        this.dropTarget = target;
        applyBehavior();
    }

    /* --- Body ------------------------------- */

    /**
     * Starts the drag gesture, detecting where the mouse clicked the node.
     * 
     * @param e The mouse event.
     */
    protected void dragStart(MouseEvent e) {
        if (!statusCheck.test(e))
            return;

        // When we drag we want the Node to be in its original size
        if (source.getScaleX() != 1.0 || source.getScaleY() != 1.0) {
            source.setScaleX(1.0);
            source.setScaleY(1.0);
        }

        mouseAnchorX = e.getSceneX() - source.getTranslateX();
        mouseAnchorY = e.getSceneY() - source.getTranslateY();
    }

    /**
     * Translates the source node so that it follows the mouse.
     * 
     * @param e The mouse event.
     */
    protected void dragRunning(MouseEvent e) {
        if (!statusCheck.test(e))
            return;

        source.setTranslateX(e.getSceneX() - mouseAnchorX);
        source.setTranslateY(e.getSceneY() - mouseAnchorY);
    }

    /**
     * Plays an animation when the drag source is dropped on the target.
     */
    protected void animateDrop() {
        ScaleTransition zoom = new ScaleTransition(Duration.millis(150.0), source);
        zoom.setByX(0.3);
        zoom.setByY(0.3);
        ScaleTransition zoomOut = new ScaleTransition(Duration.millis(150.0), source);
        zoomOut.setByX(-0.3);
        zoomOut.setByY(-0.3);
        zoomOut.setDelay(Duration.millis(100.0));

        zoom.setOnFinished(e -> zoomOut.play());

        RotateTransition alignToTarget = new RotateTransition(Duration.millis(300.0), source);
        alignToTarget.setByAngle(dropTarget.getRotate() - source.getRotate());

        TranslateTransition moveToCenter = new TranslateTransition(Duration.millis(100.0), source);
        double xSource = source.localToScene(source.getBoundsInLocal()).getCenterX();
        double ySource = source.localToScene(source.getBoundsInLocal()).getCenterY();
        double xTarget = dropTarget.localToScene(dropTarget.getBoundsInLocal()).getCenterX();
        double yTarget = dropTarget.localToScene(dropTarget.getBoundsInLocal()).getCenterY();
        moveToCenter.setByX(xTarget - xSource); // xSource + translate = xPz -> translate = xPz-xSource
        moveToCenter.setByY(yTarget - ySource);
        moveToCenter.setOnFinished(e -> {
            alignToTarget.play();
            zoom.play();
        });
        moveToCenter.play();
    }

    /**
     * Translates the source node to its original position if the node <em>has
     * not</em> be dropped on the target.
     * 
     * @param e The mouse event.
     */
    protected void animateReset(MouseEvent e) {
        TranslateTransition reset = new TranslateTransition(Duration.millis(300.0), source);
        reset.setByX(mouseAnchorX - e.getSceneX()); // correspond to setTranslateX(0);
        reset.setByY(mouseAnchorY - e.getSceneY()); // correspond to setTranslateY(0);
        reset.play();
    }

    
    /** 
     * @param e
     * @return boolean
     */
    /* --- Behavior --------------------------- */

    @Override
    public boolean behave(MouseEvent e) {
        if (e.getButton().equals(MouseButton.SECONDARY))
            return false;

        if (e.getPickResult().getIntersectedNode() != dropTarget) {
            animateReset(e);
            return false;
        }

        animateDrop();
        state = true;
        return true;
    }

    @Override
    protected void applyBehavior() {
        source.setOnMousePressed(this::dragStart);
        source.setOnMouseDragged(this::dragRunning);
        source.setOnMouseReleased(this::onEnd);
    }
}