package controller;

import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import view.animations.UnoText;

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
        UnoText.play((Pane) unoNode.getParent());
    }
}
