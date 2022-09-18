package controller;

import events.toModel.InputType;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import prefabs.Card;

/**
 * Through this control a card can be dragged, and if it drops into a target,
 * the <code>InputListener</code> will be notified.
 */
public class DragAndDrop extends Control {
    private static DragAndDrop instance;

    public static DragAndDrop getInstance() {
        if (instance == null)
            instance = new DragAndDrop();
        return instance;
    }

    private double mouseAnchorX;
    private double mouseAnchorY;
    private Node target;

    private void dragStart(MouseEvent e, Node source) {
        // When we drag we want the Node to be in its original size
        if (source.getScaleX() != 1.0 || source.getScaleY() != 1.0) {
            source.setScaleX(1.0);
            source.setScaleY(1.0);
        }

        mouseAnchorX = e.getSceneX() - source.getTranslateX();
        mouseAnchorY = e.getSceneY() - source.getTranslateY();
    }

    private void dragRunning(MouseEvent e, Node source) {
        source.setTranslateX(e.getSceneX() - mouseAnchorX);
        source.setTranslateY(e.getSceneY() - mouseAnchorY);
    }

    private void dragEnd(MouseEvent e, Node source, Card inputToSend) {
        if (e.getButton().equals(MouseButton.SECONDARY))
            return;

        if (e.getPickResult().getIntersectedNode() == target)
            animatePlaying(source, __ -> listener.acceptInput(InputType.TURN_DECISION, inputToSend));
        else {
            TranslateTransition reset = new TranslateTransition(Duration.millis(300.0), source);
            reset.setByX(mouseAnchorX - e.getSceneX()); // correspond to setTranslateX(0);
            reset.setByY(mouseAnchorY - e.getSceneY()); // correspond to setTranslateY(0);
            reset.play();
        }
    }

    /**
     * Plays an animation when the drag source is dropped on the target. After that
     * animation, an action is performed.
     * 
     * @param source   The drag source.
     * @param onFinish The action to perform after the animation.
     */
    private void animatePlaying(Node source, EventHandler<ActionEvent> onFinish) {
        ScaleTransition zoom = new ScaleTransition(Duration.millis(150.0), source);
        zoom.setByX(0.3);
        zoom.setByY(0.3);
        ScaleTransition zoomOut = new ScaleTransition(Duration.millis(150.0), source);
        zoomOut.setByX(-0.3);
        zoomOut.setByY(-0.3);
        zoomOut.setDelay(Duration.millis(100.0));

        zoom.setOnFinished(e -> {
            zoomOut.play();
            onFinish.handle(e);
        });

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

    /**
     * Sets the node that will cause this to send the input to the
     * <code>InputListener</code>.
     * 
     * @param target A node considered as the drop target for the drag and drop
     *               gesture.
     */
    public void setDropTarget(Node target) {
        this.target = target;
    }

    @Override
    public void setControls(Card card) {
        Node dragSource = card.getGuiContainer();
        dragSource.setOnMousePressed(e -> dragStart(e, dragSource));
        dragSource.setOnMouseDragged(e -> dragRunning(e, dragSource));
        dragSource.setOnMouseReleased(e -> dragEnd(e, dragSource, card));
    }
}