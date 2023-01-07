public interface AppState {
    /**
     * Instantiates all the elements laying in the scene with their style and
     * functionalities. This method <em>do not</em> arranges those elements in the
     * scene.
     */
    public void createElements();

    /**
     * Arranges all elements in the scene.
     */
    public void arrangeElements();

    public void display();
}
