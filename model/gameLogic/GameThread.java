package model.gameLogic;

import model.gameEntities.Player;

/* --- Mine ------------------------------- */

/**
 * A thread that runs a single UNO game.
 */
public abstract class GameThread {
    /* --- Fields ----------------------------- */

    private static Thread oldGame;
    private static Thread currentGame;

    /* --- Body ------------------------------- */

    /**
     * Sets a new thread. It is possible to instantiate only one thread at time,
     * thus if the old thread is still running, it will be interrupted.
     * 
     * @param newThread   The new thread.
     */
    private static void setThread(Thread newThread) {
        oldGame = currentGame;
        currentGame = newThread;
    }

    /**
     * Sets a new thread that runs a game and starts it.
     */
    public static void play(Player[] players) {
        Thread newGame = new Thread(() -> {
            Game.getInstance().interruptGame();
            if (oldGame != null) {
                try {
                    oldGame.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Game.getInstance().setupGame(players);
            Game.getInstance().play();
        });
        newGame.setName("UNO-game");
        setThread(newGame);
        currentGame.start();
    }

    /**
     * Sets the current thread to null, therefore interrupting it.
     * 
     * @param isInterrupt False if the game ended internally, i.e. after someone
     *                    won, true otherwise.
     */
    public static void stop(boolean isInterrupt) {
        Thread gameKiller = new Thread(() -> {
            Game.getInstance().interruptGame();
            if (oldGame != null) {
                try {
                    oldGame.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            currentGame = null;
        });
        gameKiller.setName("UNO-game-killer");
        setThread(gameKiller);
        currentGame.start();
    }
}
