package events;

/**
 * All notifiable events.
 */
public enum EventType {
    PLAYER_HAND_INCREASE(EventCategory.CHANGE),
    PLAYER_HAND_DECREASE(EventCategory.CHANGE),
    CARD_CHANGE(EventCategory.CHANGE),
    TURN_BLOCKED(EventCategory.CHANGE),

    PLAYER_PLAYED_CARD(EventCategory.PLAYER_ACTION),
    PLAYER_DREW(EventCategory.PLAYER_ACTION),
    USER_SELECTING_CARD(EventCategory.PLAYER_ACTION),
    USER_DREW(EventCategory.PLAYER_ACTION),
    USER_PLAYED_CARD(EventCategory.PLAYER_ACTION),
    UNO_DECLARED(EventCategory.PLAYER_ACTION),
    INVALID_CARD(EventCategory.PLAYER_ACTION),

    XP_EARNED(EventCategory.USER_INFO),
    NEW_LEVEL_PROGRESS(EventCategory.USER_INFO),
    LEVELED_UP(EventCategory.USER_INFO),
    USER_WON(EventCategory.USER_INFO),
    USER_PLAYED_GAME(EventCategory.USER_INFO),
    USER_NEW_NICK(EventCategory.USER_INFO),
    USER_NEW_ICON(EventCategory.USER_INFO),
    INFO_RESET(EventCategory.USER_INFO),

    PLAYER_WON(),
    NEW_CARD(),

    TURN_START(EventCategory.TURN),
    TURN_END(EventCategory.TURN),

    GAME_READY(EventCategory.SETUP),
    GAME_START(EventCategory.SETUP),
    GAME_END(EventCategory.SETUP),
    RESET(EventCategory.SETUP),

    ADD(EventCategory.CONTROL);

    EventCategory category;

    /**
     * Gets this event category.
     * 
     * @return The event category.
     */
    public EventCategory getCategory() {
        return category;
    }

    private EventType(EventCategory category) {
        this.category = category;
    }

    private EventType() {
        category = EventCategory.UNCLASSIFIED;
    }
}
