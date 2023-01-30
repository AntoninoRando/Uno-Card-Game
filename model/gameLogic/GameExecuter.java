package model.gameLogic;

/* --- Mine ------------------------------- */

/**
 * A thread that runs a single UNO game.
 */
public abstract class GameExecuter {
    /* --- Fields ----------------------------- */

    private static Thread dyingGame;
    private static Thread ongoingGame;

    /* --- Body ------------------------------- */

    /**
     * Sets a new thread. It is possible to instantiate only one thread at time,
     * thus if the old thread is still running, it will be interrupted.
     * 
     * @param newThread The new thread.
     */
    private static void setThread(Thread newGame) {
        dyingGame = ongoingGame;
        ongoingGame = newGame;
    }

    /**
     * Sets a new thread that runs a game and starts it.
     */
    public static void play() {
        Thread newGame = new Thread(() -> {
            if (Game.isInstantiated())
                Game.interruptGame();

            try {
                dyingGame.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
            }

            Game.play();
        });
        newGame.setName("UNO-game");
        setThread(newGame);
        newGame.start();
    }

    /**
     * Sets the current thread to null, therefore interrupting it.
     * 
     * @param isInterrupt False if the game ended internally, i.e. after someone
     *                    won, true otherwise.
     */
    public static void stop(boolean isInterrupt) {
        Thread gameKiller = new Thread(() -> {
            if (Game.isInstantiated())
                Game.interruptGame();

            try {
                dyingGame.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
            }

            ongoingGame = null;
        });
        gameKiller.setName("UNO-game-killer");
        setThread(gameKiller);
        ongoingGame.start();
    }
}
