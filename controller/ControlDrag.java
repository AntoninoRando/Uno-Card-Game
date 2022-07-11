package controller;

import java.util.Optional;

import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import model.cards.Card;
import view.CardContainer;

public class ControlDrag extends Control {
    public ControlDrag(Card card, Controller handler) {
        super(handler);

        execute = () -> handler.sendInput(card);
        applyDraggability(card.getGuiContainer());
    }

    /* DRAGGABILITY */
    /* ------------- */
    // TODO c'e un bug che se dragghi una carta mentre sta tornando nella posizione iniziale, quella vola via
    private double mouseAnchorX;
    private double mouseAnchorY;
    private static Optional<Bounds> target = Optional.empty();

    public static void setDontResetZone(Bounds bounds) {
        target = Optional.of(bounds);
    }

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
        if (!target.isPresent() || !target.get().contains(e.getSceneX(), e.getSceneY())) {
            TranslateTransition reset = new TranslateTransition(Duration.millis(300.0), source);
            reset.setByX(mouseAnchorX - e.getSceneX()); // correspond to setTranslateX(0);
            reset.setByY(mouseAnchorY - e.getSceneY()); // correspond to setTranslateY(0);
            reset.play();
        } else
            execute.run();
    }

    public void applyDraggability(CardContainer source) {
        source.setOnMousePressed(e -> dragStart(e, source));
        source.setOnMouseDragged(e -> dragRunning(e, source));
        source.setOnMouseReleased(e -> dragEnd(e, source));
    }
}