package model;

import model.gameLogic.Game;

/* --- Mine ------------------------------- */

/**
 * This class is a thread that runs the UNO game logic.
 */
public abstract class GameThread {
    /* --- Fields ----------------------------- */

    private static Thread thread;

    /* --- Body ------------------------------- */

    public static void play() {
        thread = new Thread(() -> Game.getInstance().play());
        thread.start();
    }

    // public static void stop(boolean isInterrupt) {
    //     Loop.getInstance().endGame(isInterrupt);
    //     thread.interrupt();
    //     try {
    //         thread.join(); // Waiting for this thread to die before resuming
    //     } catch (InterruptedException e) {
    //     }
    // }

    // public static void pause() {
    //     Loop.getInstance().setPause(true);
    // }

    // public static void resume() {
    //     Loop.getInstance().setPause(false);
    // }
}
