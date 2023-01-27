package model.gameLogic;

/**
 * Action performed by players to resolve their turn.
 */
public enum Action {
    FROM_DECK_DRAW("Draw a card from the main deck."),
    FROM_HAND_PLAY_CARD("Select a card from hand."),
    FROM_HAND_PLAY_TAG("Select a tag of a card in hand."),
    SAY_UNO("Say uno to avoid malus."),
    SELECTION_COMPLETED("Select a card among alternatives."),
    SKIP("Skip the turn doing nothing. Expecially usefull when user quits the game.");

    private String description;

    private Action(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}