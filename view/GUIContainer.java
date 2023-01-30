package view;

/**
 * A GUI element made up of severals GUI elements.
 */
public interface GUIContainer {
    /**
     * Instantiates all elements contained in this.
     */
    public void createElements();

    /**
     * Arranges and styles all elements contained in this, included this container
     * itself.
     */
    public void arrangeElements();

    /**
     * Applies behaviors to all elements contained in this.
     */
    public void applyBehaviors();

    /**
     * Makes this ready to be displayed.
     */
    default void initialize() {
        createElements();
        arrangeElements();
        applyBehaviors();
    }
}
