package view;

/**
 * A GUI element made up of different GUI elements.
 */
public interface GUIContainer {
    /**
     * Instantiates all the elements laying in this, with their style and
     * functionalities. This method <em>do not</em> arranges those elements.
     */
    public void createElements();

    /**
     * Arranges all elements in this.
     */
    public void arrangeElements();

    /**
     * Apply behaviors to the elements inside the GUI container.
     */
    public void applyBehaviors();

    /**
     * TODO
     */
    default void initialize() {
        createElements();
        arrangeElements();
        applyBehaviors();
    }
}
