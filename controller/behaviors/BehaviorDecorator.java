package controller.behaviors;

import java.util.function.Consumer;

import javafx.scene.input.InputEvent;

public class BehaviorDecorator<T extends InputEvent> {
    protected Behavior<T> wrappee;

    public BehaviorDecorator(Behavior<T> wrappee) {
        this.wrappee = wrappee;
        wrappee.setEnd(this::onEnd);
    }

    public void setEnd(Consumer<T> behavior) {
        wrappee.setEnd(behavior);   
    }

    public void onEnd(T e) {
        wrappee.onEnd(e);
    }
}
