package model.gameLogic;

/* --- JUno ------------------------------- */

/**
 * A thread holder that runs a single game math. This class makes it possible to
 * have only one match active at run-time.
 */
public abstract class GameExecuter {
    /* --- Fields ----------------------------- */

    /**
     * Old active game that will be disabled and killed.
     */
    private static Game dyingGame;
    /**
     * New game that will be activated.
     */
    private static Game ongoingGame;
    /**
     * The thread in which the old game dwelt.
     */
    private static Thread oldGameExecuter;
    /**
     * The thread in which the new game dwells.
     */
    private static Thread currentGameExecuter;

    /* --- Body ------------------------------- */

    /**
     * Sets a new thread that runs a game and starts it. If an old game is still
     * active, kills it first.
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
     * Kills the current active game and the thread that holds it.
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
