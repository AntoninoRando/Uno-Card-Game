package model.events;

import model.Player;

public interface InputListener {
    public abstract void accept(int choice, Player source); 

    public abstract void accept(String choice, Player source);
}