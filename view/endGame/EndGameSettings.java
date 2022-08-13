package view.endGame;

import javafx.scene.control.Label;
import javafx.scene.control.Button;

public abstract class EndGameSettings {
    public static final GameResults GAME_RESULTS = new GameResults();

    public static void addButtons(Button... buttons) {
        GAME_RESULTS.buttons.getChildren().addAll(buttons);
    }

    public static void updateGameResults(String winnerNick, int xpEarned) {
        ((Label) GAME_RESULTS.winner.getChildren().get(1)).setText(winnerNick);
        GAME_RESULTS.updateXpEarned(xpEarned);
    }
}
