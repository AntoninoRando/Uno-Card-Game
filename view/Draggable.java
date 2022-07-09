package view;

import javafx.scene.Node;

public class Draggable {
    private double mouseAnchorX;
    private double mouseAnchorY;

    public void makeDraggable(Node n) {
        n.setOnMousePressed(e -> {
            mouseAnchorX = e.getSceneX() - n.getTranslateX();
            mouseAnchorY = e.getSceneY() - n.getTranslateY();
            n.translateZProperty().set(100);
        });

        n.setOnMouseDragged(e -> {
            n.setTranslateX(e.getSceneX() - mouseAnchorX);
            n.setTranslateY(e.getSceneY() - mouseAnchorY);
        });
    }
}
