package controller;

import events.toModel.InputType;
import prefabs.Card;

public class Select extends Control {
    @Override
    public void setControls(Card card) {
        card.getGuiContainer().setOnMouseClicked(__ -> {
            (listener != null ? listener : globalListener).acceptInput(InputType.SELECTION, card);
            if (action != null)
                fire();
        });
    }
}
