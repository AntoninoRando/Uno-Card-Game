/**
 * A new scene in the application. The term 'scene' should not be confused with
 * the JavaFX <code>Scene</code>: an app state is a scene in its general term,
 * i.e., a radical change of context in the application.
 */
@FunctionalInterface
public interface AppState {
    /**
     * Makes this scene the active scene of the application.
     */
    public void display();
}
