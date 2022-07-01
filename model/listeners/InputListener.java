package model.listeners;

@FunctionalInterface
public interface InputListener {
    public abstract void validate(int choice, int source); 
}