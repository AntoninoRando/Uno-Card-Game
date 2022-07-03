package model.events;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EventManager {
    private Map<String, List<EventListener>> listeners;

    public EventManager() {
        listeners = new HashMap<>();
    }

    public void subscribe(String eventType, EventListener listener) {
        listeners.putIfAbsent(eventType, new LinkedList<>());
        listeners.get(eventType).add(listener);
    }

    public void unsubscribe(String eventType, EventListener listener) {
        listeners.get(eventType).remove(listener);
    }

    public void notify(String eventType, Object data) {
        listeners.putIfAbsent(eventType, new LinkedList<>());
        listeners.get(eventType).forEach(e -> e.update(data));
    }
}