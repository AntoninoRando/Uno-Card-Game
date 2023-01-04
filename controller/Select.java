package controller;

import events.toModel.InputType;
import javafx.scene.Node;

public class Select extends Control {
    @Override
    public void setControls(Node card, Object inputToSend) {
        card.setOnMouseClicked(__ -> {
            (listener != null ? listener : globalListener).acceptInput(InputType.SELECTION, inputToSend);
            if (action != null)
                fire();
        });
    }
}
