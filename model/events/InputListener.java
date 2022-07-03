package model.events;

import model.Player;

public interface InputListener {
    public abstract void validate(int choice, Player source); 

    public abstract void validate(String choice, Player source);
}