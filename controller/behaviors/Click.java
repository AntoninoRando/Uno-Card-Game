package controller.behaviors;

import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class Click extends Behavior<MouseEvent> {
    /* --- Fields ----------------------------- */

    protected Node source;
    protected boolean[] quantity; // le length of the array is the quantity of clicks to perform the action, the
                                  // value false is the left click and true the right click.
    protected double[] intervals;

    /* --- Constructors ----------------------- */

    public Click(Node source, boolean[] quantity, double[] intervals) {
        this.source = source;
        this.quantity = quantity;
        this.intervals = intervals;
        applyBehavior();
    }

    /* --- Body ------------------------------- */

    protected void applyBehavior() {
        // Instead of setOnMouseClick, this way doesn't ovveride previous click behavior
        source.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onEnd);
    }

    @Override
    public boolean behave(MouseEvent e) {
        // TODO implementare quantity e intervals per esteso
        // When we drag we want the Node to be in its original size
        if (!e.getButton().equals(!quantity[0] ? MouseButton.PRIMARY : MouseButton.SECONDARY))
            return false;

        if (e.getClickCount() != quantity.length)
            return false;

        return true;
    }
}
