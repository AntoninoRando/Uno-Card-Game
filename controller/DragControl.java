package controller;

import java.util.Optional;

import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import model.cards.Card;
import view.CardContainer;

public class DragControl extends Control {
    private Card card;
    private CardContainer toDrag;

    public DragControl(Card toDrag, Controller handler) {
        super(handler);

        card = toDrag;
        this.toDrag = card.getGuiContainer();
        execute = () -> handler.sendInput(card);
        applyDraggability();
    }

    /* DRAGGABILITY */
    /* ------------- */
    private double mouseAnchorX;
    private double mouseAnchorY;
    private static Optional<Bounds> dontResetZone = Optional.empty();

    public static void setDontResetZone(Bounds bounds) {
        dontResetZone = Optional.of(bounds);
    }

    private void dragStart(MouseEvent e) {
        // When we drag we want the Node to be in its original size
        if (toDrag.getScaleX() != 1.0 || toDrag.getScaleY() != 1.0) {
            toDrag.setScaleX(1.0);
            toDrag.setScaleY(1.0);
        }
        mouseAnchorX = e.getSceneX() - toDrag.getTranslateX();
        mouseAnchorY = e.getSceneY() - toDrag.getTranslateY();
    }

    private void dragRunning(MouseEvent e) {
        toDrag.setTranslateX(e.getSceneX() - mouseAnchorX);
        toDrag.setTranslateY(e.getSceneY() - mouseAnchorY);
    }

    private void dragEnd(MouseEvent e) {
        if (!dontResetZone.isPresent() || !dontResetZone.get().contains(e.getSceneX(), e.getSceneY())) {
            TranslateTransition reset = new TranslateTransition(Duration.millis(300.0), toDrag);
            reset.setByX(mouseAnchorX - e.getSceneX()); // setTranslateX(0);
            reset.setByY(mouseAnchorY - e.getSceneY()); // setTranslateY(0);
            reset.play();
        } else
            execute.run();
    }

    public void applyDraggability() {
        toDrag.setOnMousePressed(this::dragStart);
        toDrag.setOnMouseDragged(this::dragRunning);
        toDrag.setOnMouseReleased(this::dragEnd);
    }
}