package controller.behaviors;

import javafx.scene.Node;
import javafx.scene.input.KeyEvent;

public class KeyPress extends Behavior<KeyEvent> {
    /* --- Fields ----------------------------- */

    protected Node source;
    protected int keyCode;

    /* --- Constructors ----------------------- */

    public KeyPress(Node source, int keyCode) {
        this.source = source;
        this.keyCode = keyCode;
        applyBehavior();
    }

    /* --- Body ------------------------------- */

    protected void applyBehavior() {
        // Instead of setOnMouseClick, this way doesn't ovveride previous click behavior
        source.addEventHandler(KeyEvent.KEY_PRESSED, this::onEnd);
    }

    @Override
    public boolean behave(KeyEvent e) {
        return e.getCode().getCode() == keyCode;
    }
}
