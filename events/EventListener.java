package events;

import java.util.Map;

/**
 * Any class implementing this interface is able to change its state when
 * notified from outside. To perform the change, this class must be notified
 * about the type of event that occurred and the data associated. The changes
 * must be implemented on the <code>update</code> method.
 */
@FunctionalInterface
public interface EventListener {
    /**
     * Changes the internal state of this based on an external event.
     * 
     * @param event The type of event that this interface should listen to.
     * @param data  The data used to gather information about the external event.
     */
    public void update(Event event, Map<String, Object> data);

    /**
     * Method to run inside the <code>update</code> method in case this didn't
     * implemented the given event associated with the given Object data.
     * 
     * @param event The eventlistened but not implemented.
     * @param data  The data associated to the event not expected.
     * @throws Error
     */
    default void throwUnsupportedError(Event event, Object data) throws Error {
        throw new Error(this + " was listening for " + event + " with " + data
                + " as data, but it does not support this event!");
    }

    /**
     * Sets the priority of the events.
     * 
     * @param event The event notified.
     * @return The priority of the event notified; the bigger the number, the
     *         greater is the priority.
     */
    default int getEventPriority(Event event) {
        return 1;
    }

    /**
     * Used to compare two eventListeners' event priority.
     * 
     * @param event         The same type of event.
     * @param otherListener The other listener used to perform the comparison.
     * @return An integer that tells which event listener as a greater priority.
     */
    default int compareTo(Event event, EventListener otherListener) {
        return -Integer.compare(getEventPriority(event), otherListener.getEventPriority(event));
    }
}