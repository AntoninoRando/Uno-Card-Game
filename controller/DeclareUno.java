package controller;

public class DeclareUno extends Control {
    private static DeclareUno instance;

    public static DeclareUno getInstance() {
        if (instance == null)
            instance = new DeclareUno();
        return instance;
    }

    private DeclareUno() {
        setAction(listener -> listener.acceptInput("unoDeclared"));
    }
}
