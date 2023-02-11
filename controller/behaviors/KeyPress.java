package controller.behaviors;

import javafx.scene.Node;
import javafx.scene.input.KeyEvent;

/**
 * A specific key pressed on the keyboard event. It exhibites as soon as the
 * user press down a given key on the keyboard.
 */
public class KeyPress extends Behavior<KeyEvent> {
    /* --- Fields ----------------------------- */

    protected int keyCode;

    /* --- Constructors ----------------------- */

    public KeyPress(Node source, int keyCode) {
        this.source = source;
        this.keyCode = keyCode;
        applyBehavior();
    }

    /* --- Body ------------------------------- */

    @Override
    protected void applyBehavior() {
        // Instead of setOnMouseClick, this way doesn't ovveride previous click behavior
        source.addEventHandler(KeyEvent.KEY_PRESSED, this::onEnd);
    }

    
    /** 
     * @param e
     * @return boolean
     */
    @Override
    public boolean behave(KeyEvent e) {
        return e.getCode().getCode() == keyCode;
    }
}
