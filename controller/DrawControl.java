package controller;

import javafx.scene.Node;

public class DrawControl extends Control {
    private Node drawNode;

    protected DrawControl(Node drawNode, Controller handler) {
        super(handler);
        this.drawNode = drawNode;
        execute = () -> handler.sendInput("draw");
        applyDrawability();
    }

    public void applyDrawability() {
        drawNode.setOnMouseClicked(e -> execute.run());
    }
}
