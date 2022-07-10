package controller;

public abstract class Control {
    protected Control(Controller handler) {
        handler.addControl(this);
    }

    protected Runnable execute; // TODO non dovrebbe essere un runnable
}
