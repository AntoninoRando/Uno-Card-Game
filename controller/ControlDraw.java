package controller;

import javafx.scene.Node;

public class ControlDraw extends Control {
    private Node drawNode;

    protected ControlDraw(Node drawNode, Controller handler) {
        super(handler);
        this.drawNode = drawNode;
        execute = () -> handler.sendInput("draw");
        applyDrawability();
    }

    public void applyDrawability() {
        drawNode.setOnMouseClicked(e -> execute.run());
    }
}
