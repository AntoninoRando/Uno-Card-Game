package model.listeners;

@FunctionalInterface
public interface InvalidActionListener {
    public void warn(String message);
}
