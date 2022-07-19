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

            if (e.getClickCount() == 2) 
                execute.run();
        });
    }
}
