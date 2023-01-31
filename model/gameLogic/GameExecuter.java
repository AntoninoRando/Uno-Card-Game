package model.gameLogic;

/* --- Mine ------------------------------- */

/**
 * A thread that runs a single UNO game.
 */
public abstract class GameExecuter {
    /* --- Fields ----------------------------- */

    private static Game dyingGame;
    private static Game ongoingGame;
    private static Thread oldGameExecuter;
    private static Thread currentGameExecuter;

    /* --- Body ------------------------------- */


    /**
     * Sets a new thread that runs a game and starts it.
     */
    public static void playNewGame() {
        dyingGame = ongoingGame;
        oldGameExecuter = currentGameExecuter;

        GameExecuter.ongoingGame = new Game();
        Thread newGameExecuter = new Thread(() -> {
            if (dyingGame != null)
                dyingGame.block();
            
            try {
                oldGameExecuter.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
            }

            ongoingGame.play();
        });

        GameExecuter.currentGameExecuter = newGameExecuter;
        newGameExecuter.setName("UNO-game");
        newGameExecuter.start();
    }

    /**
     * Sets the current thread to null, therefore interrupting it.
     * 
     * @param isInterrupt False if the game ended internally, i.e. after someone
     *                    won, true otherwise.
     */
    public static void stop() {
        dyingGame = ongoingGame;
        oldGameExecuter = currentGameExecuter;

        GameExecuter.ongoingGame = null;
        Thread gameKiller = new Thread(() -> {
            if (dyingGame != null)
                dyingGame.block();
            
            try {
                oldGameExecuter.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
            }
        });

        GameExecuter.currentGameExecuter = gameKiller;
        gameKiller.setName("UNO-game-KILLER");
        gameKiller.start();
    }
}
