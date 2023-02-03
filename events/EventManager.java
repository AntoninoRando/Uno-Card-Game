package events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A mediator between the <code>EventListener</code> and the objects that notify
 * changes. This enables to update at once all <code>EventListener</code>
 * listening for a specific event.
 */
public class EventManager {
    /* --- Fields ----------------------------- */

    protected Map<Event, List<EventListener>> listeners;

    /* --- Constructors ----------------------- */

    public EventManager() {
        listeners = new HashMap<>();
    }

    /* --- Observer --------------------------- */

    /**
     * From this method call on, an <code>EventListener</code> will be updated if
     * any of the given events occur.
     * 
     * @param listener The listener that will be updated whenever one of these
     *                 events happens.
     * @param events   All the events that the listener is listening.
     */
    public void subscribe(EventListener listener, Event... events) {
        for (Event event : events) {
            listeners.putIfAbsent(event, new ArrayList<>());
            listeners.get(event).add(listener);
            listeners.get(event).sort((e1, e2) -> e1.compareTo(event, e2));
        }
    }

    /**
     * From this method call on, an <code>EventListener</code> will <em>not</em> be
     * updated if any of the given events occur.
     * 
     * @param listener The listener that will stop to listen for the given event.
     * @param events   Events that will not update the listener anymore.
     */
    public void unsubscribe(EventListener listener, Event... events) {
        for (Event event : events)
            listeners.get(event).remove(listener);
    }

    /**
     * Updates all the <code>EventListener</code> that were listening to the given
     * event.
     * 
     * @param event The event that occurred.
     * @param data  The data that describes the event.
     */
    public void notify(Event event, Map<String, Object> data) {
        if (!listeners.containsKey(event))
            return;
        listeners.get(event).forEach(listener -> listener.update(event, data));
    }
}