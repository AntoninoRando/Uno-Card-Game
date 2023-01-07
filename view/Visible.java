package view;

public interface Visible {
    /**
     * Instantiates all the elements laying in this, with their style and
     * functionalities. This method <em>do not</em> arranges those elements.
     */
    public void createElements();

    /**
     * Arranges all elements in this.
     */
    public void arrangeElements();
}
