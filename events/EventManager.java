package events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.gameLogic.Card;
import model.gameLogic.Player;

// TODO gestire meglio gli eventi del tipo che se ci sono due eventi con lo stesso tipo vengono accorpati in un solo metodo da eseguire
public class EventManager {
    /* OBSERVER PATTERN */
    /* ---------------- */
    private Map<EventType, List<EventListener>> listeners;

    public EventManager() {
        listeners = new HashMap<>();
    }

    // TODO aggiungere che ci si può iscrivere per ascoltare categorie e non
    // specifici eventi
    public void subscribe(EventListener listener, EventType... events) {
        for (EventType event : events) {
            listeners.putIfAbsent(event, new ArrayList<>());
            listeners.get(event).add(listener);
            listeners.get(event).sort((e1, e2) -> e1.compareTo(event, e2));
        }
    }

    public void unsubscribe(EventType event, EventListener listener) {
        listeners.get(event).remove(listener);
    }

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