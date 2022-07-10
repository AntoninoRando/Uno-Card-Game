package controller;

import java.util.LinkedList;
import java.util.List;

import model.Player;
import model.events.InputListener;

/**
 * This class enables a player to take choices (input) that modify the game
 * state.
 */
public abstract class Controller extends Thread {
    protected Player source;
    protected InputListener inputListener;
    protected List<Control> controls;

    public Controller() {
        controls = new LinkedList<>();
    }

    public void addControl(Control control) {
        controls.add(control);
    }

    public void sendInput(Object choice) {
        inputListener.accept(choice, source);
    }

    public abstract void setupPlayer();

    /* GETTERS AND SETTERS */
    /* ------------------- */
    public Player getSource() {
        return source;
    }

    public void setSource(Player s) {
        source = s;
    }

    public void setInputListener(InputListener l) {
        inputListener = l;
    }
}