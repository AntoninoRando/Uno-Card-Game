package model;

import model.gameLogic.Loop;

public abstract class GameThread {
    private static Thread thread;

    public static void play() {
        thread = new Thread(() -> Loop.getInstance().play());
        thread.start();
    }

    public static void stop(boolean isInterrupt) {
        Loop.getInstance().endGame(isInterrupt);
        thread.interrupt();
        try {
            thread.join(); // Waiting for this thread to die before resuming
        } catch (InterruptedException e) {
        }
    }

    public static void pause() {
        Loop.getInstance().setPause(true);
    }

    public static void resume() {
        Loop.getInstance().setPause(false);
    }
}
