package events.toController;

import javafx.scene.Node;

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
    default void setControls(Node element, Object intputToSend) {
    }
}
