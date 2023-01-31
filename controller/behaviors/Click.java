package controller.behaviors;

import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * Mouse click event. It exhibites when the user performs a sequence of clicks,
 * right or left, each within a given interval.
 */
public class Click extends Behavior<MouseEvent> {
    /* --- Fields ----------------------------- */

    /**
     * The lenght of the array is the quantity of clicks that must be performed to
     * exhibite the behavior. The <code>false</code> values are left clicks, and
     * <code>true</code> values are right clicks.
     */
    protected boolean[] quantity;
    protected double[] intervals;

    /* --- Constructors ----------------------- */

    /**
     * Applies the click-detect behavior to the source node.
     * 
     * @param source    The node that will hold this behavior.
     * @param quantity  The amount of clicks to be performed and their type (false
     *                  if left click, true if right click).
     * @param intervals The intervals between each click.
     */
    public Click(Node source, boolean[] quantity, double[] intervals) {
        this.source = source;
        this.quantity = quantity;
        this.intervals = intervals;
        applyBehavior();
    }

    /* --- Behavior --------------------------- */

    @Override
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
