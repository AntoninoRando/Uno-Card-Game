package model.events;

public interface EventListener {
    public void update(String eventLabel, Object... data);
}
