package events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import prefabs.Card;
import prefabs.Player;

/**
 * A class storing all the <code>EventListener</code>s to update in relation to
 * the event notified. This enables to update multiple classes at once.
 */
public class EventManager {
    private Map<EventType, List<EventListener>> listeners;

    public EventManager() {
        listeners = new HashMap<>();
    }

    /**
     * Sets a listener ready for listening the events specified.
     * 
     * @param listener The listener that will be updated whenever one of these
     *                 events happens.
     * @param events   All the events that the listener is listening.
     */
    public void subscribe(EventListener listener, EventType... events) {
        for (EventType event : events) {
            listeners.putIfAbsent(event, new ArrayList<>());
            listeners.get(event).add(listener);
            listeners.get(event).sort((e1, e2) -> e1.compareTo(event, e2));
        }
    }

    /**
     * Stops the given listener to listen for the event specified.
     * 
     * @param event    Event that will not update the listener anymore.
     * @param listener The listener that will stop to listen for the given event.
     */
    public void unsubscribe(EventType event, EventListener listener) {
        listeners.get(event).remove(listener);
    }

    /**
     * Updates all the listener that were listening for the given event.
     * 
     * @param event The event to notify.
     */
    public void notify(EventType event) {
        if (!listeners.containsKey(event))
            return;
        listeners.get(event).forEach(listener -> listener.update(event));
    }

    public void notify(EventType event, Object... data) {
        if (!listeners.containsKey(event))
            return;
        listeners.get(event).forEach(listener -> listener.update(event, data));
    }

    public void notify(EventType event, String data) {
        if (!listeners.containsKey(event))
            return;
        listeners.get(event).forEach(listener -> listener.update(event, data));
    }

    public void notify(EventType event, int data) {
        if (!listeners.containsKey(event))
            return;
        listeners.get(event).forEach(listener -> listener.update(event, data));
    }

    public void notify(EventType event, double data) {
        if (!listeners.containsKey(event))
            return;
        listeners.get(event).forEach(listener -> listener.update(event, data));
    }

    public void notify(EventType event, Card data) {
        if (!listeners.containsKey(event))
            return;
        listeners.get(event).forEach(listener -> listener.update(event, data));
    }

    public void notify(EventType event, Card[] data) {
        if (!listeners.containsKey(event))
            return;
        listeners.get(event).forEach(listener -> listener.update(event, data));
    }

    public void notify(EventType event, Player data) {
        if (!listeners.containsKey(event))
            return;
        listeners.get(event).forEach(listener -> listener.update(event, data));
    }

    public void notify(EventType event, Player[] data) {
        if (!listeners.containsKey(event))
            return;
        listeners.get(event).forEach(listener -> listener.update(event, data));
    }
}