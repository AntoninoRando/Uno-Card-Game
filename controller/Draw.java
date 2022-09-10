package controller;

import events.toModel.InputType;

/**
 * Through this control the user can draw a card.
 */
public class Draw extends Control {
    private static Draw instance;

    public static Draw getInstance() {
        if (instance == null)
            instance = new Draw();
        return instance;
    }

    private Draw() {
        setAction(listener -> listener.acceptInput(InputType.TURN_DECISION, "draw"));
    }
}
