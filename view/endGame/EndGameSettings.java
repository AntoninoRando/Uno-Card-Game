package view.endGame;

import javafx.scene.control.Button;

public abstract class EndGameSettings {
    public static final GameResults GAME_RESULTS = new GameResults();

    public static void addButtons(Button... buttons) {
        GAME_RESULTS.buttons.getChildren().addAll(buttons);
    }

    public static void updateWinner(String winnerIconPath, String winnerNick) {
        GAME_RESULTS.updateWinner(winnerIconPath, winnerNick);
    }

    public static void updateUser(int xpEarned, double userLevelProgress) {
        GAME_RESULTS.updateXpEarned(xpEarned, userLevelProgress);
    }
}
