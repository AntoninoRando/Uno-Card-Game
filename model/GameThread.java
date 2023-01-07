package model;

import model.gameLogic.Game;

/* --- Mine ------------------------------- */

/**
 * A thread that runs a single UNO game.
 */
public abstract class GameThread {
    /* --- Fields ----------------------------- */

    private static Thread thread;

    /* --- Body ------------------------------- */

    /**
     * Sets a new thread. It is possible to instantiate only one thread at time,
     * thus if the old thread is still running, it will be interrupted.
     * 
     * @param newThread   The new thread.
     * @param isInterrupt If the game ended normally, i.e. after someone won
     *                    (false), or if the game was interrupted before it ended
     *                    (e.g., if the user quit the app) (true).
     */
    private static void setThread(Thread newThread, boolean isInterrupt) {
        if (thread == null) {
            thread = newThread;
            return;
        }

        if (isInterrupt)
            Game.getInstance().end(isInterrupt);

        thread.interrupt();
        try {
            thread.join(); // Waiting for this thread to die before resuming
        } catch (InterruptedException e) {
        }

    }

    /**
     * Sets a new thread that runs a game and starts it.
     */
    public static void play() {
        setThread(new Thread(() -> Game.getInstance().play()), false);
        thread.start();
    }

    /**
     * Sets the current thread to null, therefore interrupting it.
     * 
     * @param isInterrupt If the game ended normally, i.e. after someone won
     *                    (false), or if the game was interrupted before it ended
     *                    (e.g., if the user quit the app) (true).
     */
    public static void stop(boolean isInterrupt) {
        setThread(null, isInterrupt);
    }
}
