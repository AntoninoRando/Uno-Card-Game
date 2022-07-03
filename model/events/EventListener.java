package model.events;

@FunctionalInterface
public interface EventListener {
    public void update(String eventType, Object data);
}
