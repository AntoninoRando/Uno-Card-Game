package model.events;

import model.Player;

@FunctionalInterface
public interface InputListener {
    public abstract void accept(Object choice, Player source);
}