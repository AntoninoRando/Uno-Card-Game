package events;

// [null, null, null, null, null, null, null]
public interface EventListener {
    public static final int cardField = 0;
    public static final int playerField = 1;
    public static final int gameField = 2;

    public void update(String eventLabel, Object[] data);

    default int getEventPriority(String eventLabel) {
        return 1;
    }

    default int compareTo(String eventLabel, EventListener otherListener) {
        return -Integer.compare(getEventPriority(eventLabel), otherListener.getEventPriority(eventLabel));
    }
}