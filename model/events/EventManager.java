package model.events;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EventManager {
    /* OBSERVER PATTERN */
    /* ---------------- */
    private Map<String, List<EventListener>> listeners;

    public EventManager() {
        listeners = new HashMap<>();
        waiting = new HashMap<>();
    }

    public void subscribe(String eventType, EventListener listener) {
        listeners.putIfAbsent(eventType, new LinkedList<>());
        listeners.get(eventType).add(listener);
    }

    public void unsubscribe(String eventType, EventListener listener) {
        listeners.get(eventType).remove(listener);
    }

    public void notify(String eventType, Object data) {
        if (listeners.containsKey(eventType))
            listeners.get(eventType).forEach(e -> e.update(eventType, data));
    }

    /* CUSTOM FIELDS */
    /* ------------- */
    private Map<EventListener, Object> waiting;

    public Object waitFor(String eventType) throws InterruptedException {
        EventManager self = this;
        EventListener e = new EventListener() {
            @Override
            public void update(String eventType, Object data) {
                waiting.put(this, data);
                synchronized (self) {
                    self.notify();
                }
            }
        };
        subscribe(eventType, e);
        synchronized (self) {
            wait();
        }
        unsubscribe(eventType, e);
        return waiting.get(e);
    }
}