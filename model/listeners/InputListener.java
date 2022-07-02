package model.listeners;

import model.Player;

@FunctionalInterface
public interface InputListener {
    public abstract void validate(int choice, Player source); 
}