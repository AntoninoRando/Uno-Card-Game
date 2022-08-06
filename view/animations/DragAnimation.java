package view.animations;

import javafx.animation.TranslateTransition;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import model.events.EventListener;
import view.gameElements.CardContainer;

public class DragAnimation implements EventListener {
    // TODO c'e un bug che se dragghi una carta mentre sta tornando nella posizione
    // iniziale, quella vola via
    private double mouseAnchorX;
    private double mouseAnchorY;

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

    private void dragReset(MouseEvent e, CardContainer source) {
        TranslateTransition reset = new TranslateTransition(Duration.millis(300.0), source);
        reset.setByX(mouseAnchorX - e.getSceneX()); // correspond to setTranslateX(0);
        reset.setByY(mouseAnchorY - e.getSceneY()); // correspond to setTranslateY(0);
        reset.play();
    }

    public void applyDraggability(CardContainer source) {
        source.setOnMousePressed(e -> dragStart(e, source));
        source.setOnMouseDragged(e -> dragRunning(e, source));
        source.setOnMouseReleased(e -> dragReset(e, source));
    }

    @Override
    public void update(String eventLabel, Object data) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(String eventLabel, Object... data) {
        // TODO Auto-generated method stub
        
    }
    
}
