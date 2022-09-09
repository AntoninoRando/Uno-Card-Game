package events;

import javafx.scene.Node;
import prefabs.Card;

/**
 * Implemented by those controller classes that wants to apply controls on new
 * GUI elements. The view is responsible of notifics.
 */
public interface ViewListener {
    /**
     * Set the new controls on the desired node.
     * 
     * @param element The GUI element which requires some controls.
     */
    default void setControls(Node element) {
    }

    /**
     * Set the new controls on the desired card node.
     * 
     * @param element The Card object, which contains a reference to its node.
     */
    default void setControls(Card data) {
    }
}
