package controller;

import events.toModel.InputType;

/**
 * Through this control the user will declare UNO!
 */
public class DeclareUno extends Control {
    private static DeclareUno instance;

    public static DeclareUno getInstance() {
        if (instance == null)
            instance = new DeclareUno();
        return instance;
    }

    private DeclareUno() {
        setAction(listener -> listener.acceptInput(InputType.TURN_DECISION, "unoDeclared"));
    }
}
