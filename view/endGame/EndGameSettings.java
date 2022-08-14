package view.endGame;

import javafx.scene.control.Button;

public abstract class EndGameSettings {
    public static final GameResults GAME_RESULTS = new GameResults();

    public static void addButtons(Button... buttons) {
        GAME_RESULTS.buttons.getChildren().addAll(buttons);
    }

    public static void updateGameResults(String winnerIconPath, String winnerNick, int xpEarned) {
        GAME_RESULTS.updateWinner(winnerIconPath, winnerNick);
        GAME_RESULTS.updateXpEarned(xpEarned);
    }
}
