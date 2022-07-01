package model.listeners;

@FunctionalInterface
public interface EndListener {
    public void playerWon(String nickname);
}