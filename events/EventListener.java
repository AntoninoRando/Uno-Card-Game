package events;

import prefabs.Card;
import prefabs.Player;

/**
 * Any class implementing this interface is able to change its state when
 * notified from outside.
 */
public interface EventListener {
    default void update(EventType event) {
    }

    default void update(EventType event, Object[] data) {
    }

    default void update(EventType event, String data) {
    }

    default void update(EventType event, int data) {
    }

    default void update(EventType event, double data) {
    }

    default void update(EventType event, Card data) {
    }

    default void update(EventType event, Card[] data) {
    }

    default void update(EventType event, Player data) {
    }

    default void update(EventType event, Player[] data) {
    }

    default void update(byte combinedEvent, Object[] data) {
    }

    default int getEventPriority(EventType event) {
        return 1;
    }

    default void throwUnsupportedError(EventType event, Object data) throws Error {
        throw new Error(this + " was listening for " + event + " with " + data
                + " as data, but it does not support this event!");
    }

    default int compareTo(EventType event, EventListener otherListener) throws Error {
        return -Integer.compare(getEventPriority(event), otherListener.getEventPriority(event));
    }
}