package model.gameLogic;

/**
 * Action toke by players during their turn.
 */
public enum Action {
    FROM_DECK_DRAW("Draw a card from the main deck."),
    FROM_HAND_PLAY_CARD("Select a card from hand."),
    FROM_HAND_PLAY_TAG("Select a tag of a card in hand."),
    SAY_UNO("Say uno to avoid malus."),
    SELECTION_COMPLETED("Select a card among alternatives."),
    INVALID("This action should not be considered as it was illegal.");

    private String description;

    private Action(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}