package controller;

import javafx.scene.Node;

public class ControlDraw extends Control {
    protected ControlDraw(Node drawNode, Controller handler) {
        super(handler);
        execute = () -> handler.sendInput("draw");
        applyDrawability(drawNode);
    }

    public void applyDrawability(Node drawNode) {
        drawNode.setOnMouseClicked(e -> execute.run());
    }
}
