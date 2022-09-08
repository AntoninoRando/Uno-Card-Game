package events;

import prefabs.Player;

@FunctionalInterface
public interface InputListener {
    public abstract void accept(Object choice, Player source);
}