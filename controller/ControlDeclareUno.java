package controller;

import javafx.scene.Node;
import javafx.scene.input.MouseButton;

public class ControlDeclareUno extends Control {
    protected ControlDeclareUno(Node unoNode, Controller handler) {
        super(handler);
        execute = () -> handler.sendInput("unoDeclared");
        applyUno(unoNode);
    }

    public void applyUno(Node unoNode) {
        unoNode.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.SECONDARY))
                return;

            if (e.getClickCount() == 2) {
                animate(unoNode);
                execute.run();
            }
        });
    }

    /* ----------------------------------- */

    public void animate(Node unoNode) {
        unoNode.setStyle("-fx-background-color: rgba(0, 255, 255, 0.50);");
    }
}
