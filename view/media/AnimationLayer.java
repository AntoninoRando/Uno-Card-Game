package view.media;

import java.util.Map.Entry;

import events.Event;
import javafx.scene.layout.Pane;

/**
 * A functional interface implemented by those UI elements that will be the
 * layer for an animation.
 * <p>
 * Its functional method is <code>getPoints</code>.
 */
@FunctionalInterface
public interface AnimationLayer {
    /**
     * Every <code>AnimationLayer</code> must implement this method. Based on the
     * type of event which require its animation, returns the layer on which the
     * animation will be played in the returned coordinates.
     * 
     * @param event
     * @return The Pane on which will be played the animation, and an array
     *         containig x coordinate, y coordinate, width and height of the
     *         animation.
     */
    public Entry<Pane, Double[]> getPoints(Event event);
}
