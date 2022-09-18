package model;

import model.gameLogic.Loop;

public abstract class GameThread {
    private static Thread thread;

    public static final void play() {
        thread = new Thread(() -> Loop.getInstance().play());
        thread.start();
    }

    public static final void stop(boolean isInterrupt) {
        Loop.getInstance().endGame(isInterrupt);
        thread.interrupt();
        try {
            thread.join(); // Waiting for this thread to die before resuming
        } catch (InterruptedException e) {
        }
    }
}
