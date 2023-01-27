package model.gameLogic;

/**
 * Possible actions performable by the user and AI during the game.
 */
public enum Action {
    FROM_DECK_DRAW("Draw a card from the main deck."),
    FROM_HAND_PLAY_CARD("Selected a card from hand."),
    FROM_HAND_PLAY_TAG("Selected a card from hand recognized by its tag."),
    SAY_UNO("Say uno to avoid malus."),
    SELECTION_COMPLETED("Selected a card among alternatives."),
    SKIP("Skip the turn doing nothing. Expecially useful when user quit the game.");

    private String description;

    private Action(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}