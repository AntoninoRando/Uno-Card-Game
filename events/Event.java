package events;

/**
 * A notifiable <em>type</em> of event.
 */
public enum Event {
    CARD_CHANGE,
    TURN_BLOCKED,

    AI_PLAYED_CARD,
    AI_DREW,
    USER_SELECTING_CARD,
    USER_DREW,
    USER_PLAYED_CARD,
    UNO_DECLARED,
    INVALID_CARD,

    INFO_CHANGE,
    INFO_RESET,

    PLAYER_WON,

    TURN_START,
    TURN_END,
    TURN_DECISION,
    SELECTION,

    GAME_READY,
    GAME_START,
}
