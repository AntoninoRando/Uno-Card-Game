package model.events;

public interface EventListener {    
    public void update(String eventLabel, Object... data);

    default int getEventPriority(String eventLabel) {
        return 1;
    }

    default int compareTo(String eventLabel, EventListener otherListener) {
        return -Integer.compare(getEventPriority(eventLabel), otherListener.getEventPriority(eventLabel));
    }
}
