package controller.controls;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import javafx.util.Duration;

import model.gameLogic.Card;

import view.gameElements.CardContainer;
import view.gameElements.TerrainPane;

public class ControlDrag extends Control {
    public ControlDrag(Card card, Node target) {
        super(null);
        this.target = target;
        action = handler -> animatePlaying(card.getGuiContainer(), e -> handler.sendInput(card));
        applyDraggability(card.getGuiContainer());
    }

    /* DRAGGABILITY */

    // TODO c'e un bug che se dragghi una carta mentre sta tornando nella posizione
    // iniziale, quella vola via
    private double mouseAnchorX;
    private double mouseAnchorY;
    private Node target;

    private void dragStart(MouseEvent e, CardContainer source) {
        // When we drag we want the Node to be in its original size
        if (source.getScaleX() != 1.0 || source.getScaleY() != 1.0) {
            source.setScaleX(1.0);
            source.setScaleY(1.0);
        }

        mouseAnchorX = e.getSceneX() - source.getTranslateX();
        mouseAnchorY = e.getSceneY() - source.getTranslateY();
    }

    private void dragRunning(MouseEvent e, CardContainer source) {
        source.setTranslateX(e.getSceneX() - mouseAnchorX);
        source.setTranslateY(e.getSceneY() - mouseAnchorY);
    }

    private void dragEnd(MouseEvent e, CardContainer source) {
        if (e.getButton().equals(MouseButton.SECONDARY))
            return;

        if (e.getPickResult().getIntersectedNode() == target)
            action.accept(handler);
        else {
            TranslateTransition reset = new TranslateTransition(Duration.millis(300.0), source);
            reset.setByX(mouseAnchorX - e.getSceneX()); // correspond to setTranslateX(0);
            reset.setByY(mouseAnchorY - e.getSceneY()); // correspond to setTranslateY(0);
            reset.play();
        }
    }

    private void animatePlaying(CardContainer source, EventHandler<ActionEvent> onFinish) {
        TranslateTransition moveToCenter = new TranslateTransition(Duration.millis(100.0), source);

        double xSource = source.localToScene(source.getBoundsInLocal()).getCenterX();
        double ySource = source.localToScene(source.getBoundsInLocal()).getCenterY();
        TerrainPane terrainCard = TerrainPane.getInstance();
        double xTc = terrainCard.localToScene(terrainCard.getBoundsInLocal()).getCenterX();
        double yTc = terrainCard.localToScene(terrainCard.getBoundsInLocal()).getCenterY();

        moveToCenter.setByX(xTc - xSource); // xSource + translate = xPz -> translate = xPz-xSource
        moveToCenter.setByY(yTc - ySource);
        moveToCenter.play();

        ScaleTransition zoom = new ScaleTransition(Duration.millis(150.0), source);
        zoom.setByX(0.3);
        zoom.setByY(0.3);
        ScaleTransition zoomOut = new ScaleTransition(Duration.millis(100.0), source);
        zoomOut.setByX(-0.3);
        zoomOut.setByY(-0.3);
        zoomOut.setDelay(Duration.millis(100.0));

        zoom.setOnFinished(e -> {
            zoomOut.play();
            onFinish.handle(e);
        });
        zoom.play();
    }

    public void applyDraggability(CardContainer source) {
        source.setOnMousePressed(e -> dragStart(e, source));
        source.setOnMouseDragged(e -> dragRunning(e, source));
        source.setOnMouseReleased(e -> dragEnd(e, source));
    }
}