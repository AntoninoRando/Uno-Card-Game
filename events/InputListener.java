package events;

import model.gameLogic.Player;

@FunctionalInterface
public interface InputListener {
    public abstract void accept(Object choice, Player source);
}