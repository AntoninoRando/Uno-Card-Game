package controller.controls;

public abstract class Controls {
    public static final Control DECLARE_UNO = new Control(handler -> handler.sendInput("unoDeclared"));

    public static final Control DRAW = new Control(handler -> handler.sendInput("draw"));
}
