package controller;

import java.util.function.Consumer;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class Control {
    protected Controller handler;
    protected Consumer<Controller> action;

    protected Control(Consumer<Controller> action) {
        this.action = action;
    }
    
    protected void setHandler(Controller handler) {
        this.handler = handler;
    }

    public EventHandler<MouseEvent> getAction() {
        return e -> action.accept(handler);
    }
}
