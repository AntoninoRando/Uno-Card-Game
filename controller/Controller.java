package controller;

import java.util.LinkedList;
import java.util.List;

import model.Player;
import model.events.EventListener;
import model.events.InputListener;

/**
 * This class enables a player to take choices (input) that modify the game
 * state.
 */
public abstract class Controller extends Thread implements EventListener {
    protected Player source;
    protected InputListener inputListener;
    protected List<Control> controls;

    public Controller() {
        controls = new LinkedList<>();
    }

    public void sendInput(Object choice) {
        inputListener.accept(choice, source);
    }

    public void addControl(Control control) {
        controls.add(control);
    }

    public abstract void setupControls();

    /* GETTERS AND SETTERS */
    /* ------------------- */
    public Player getSource() {
        return source;
    }

    public void setSource(Player source) {
        this.source = source;
        source.getHand().observers.subscribe("add", this);
    }

    public void setInputListener(InputListener inputListener) {
        this.inputListener = inputListener;
    }
}