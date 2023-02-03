package view.media;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import events.Event;
import events.EventListener;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import view.CUView;

public class AnimationHandler implements EventListener {
    /* --- Singleton -------------------------- */

    private static AnimationHandler instance;

    public static AnimationHandler getInstance() {
        if (instance == null)
            instance = new AnimationHandler();
        return instance;
    }

    private AnimationHandler() {
        layers = new HashMap<>();
    }

    /* --- Fields ----------------------------- */

    private HashMap<Event, AnimationLayer> layers;

    /* --- Body ------------------------------- */

    public static void subscribe(AnimationLayer animationLayer, Event... events) {
        for (Event event : events) {
            getInstance().layers.put(event, animationLayer); // Subscribe AnimationLayer to this
            CUView.getInstance().subscribe(getInstance(), event); // Subssribe this to CUView
        }
    }

    private Animation fetchAnimation(Event event, Map<String, Object> data) {
        Animation animation = null;

        switch (event) {
            case UNO_DECLARED:
                boolean said = (boolean) data.get("said");
                animation = said ? Animations.UNO_TEXT.get() : null;
                break;
            case AI_PLAYED_CARD:
                animation = Animations.CARD_PLAYED.get();
                break;
            case TURN_START:
                animation = Animations.FOCUS_PLAYER.get();
                animation.setWillStay(true);
                break;
            case TURN_BLOCKED:
                animation = Animations.BLOCK_TURN.get();
                break;
            default:
                throwUnsupportedError(event, data);
        }

        animation.setWillCountdown(true);
        return animation;
    }

    private void setupAndPlayAnimation(Animation animation, Event event, Map<String, Object> data) {
        Entry<Pane, Double[]> points = layers.get(event).getPoints(event);
        Pane layer = points.getKey();
        Double x = points.getValue()[0];
        Double y = points.getValue()[1];
        Double w = points.getValue()[2];
        Double h = points.getValue()[3];

        animation.setSceneCoordinates(x, y);
        animation.setDimensions(w, h);
        animation.play(layer);
    }

    @Override
    public void update(Event event, Map<String, Object> data) {
        Animation animation = fetchAnimation(event, data);

        Platform.runLater(() -> setupAndPlayAnimation(animation, event, data));

        if (animation.willCountdown()) {
            try {
                CUView.communicate(Event.PAUSE, Map.of("pause-value", true));
                animation.latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            animation.resetLatch();
            CUView.communicate(Event.PAUSE, Map.of("pause-value", false));
        }
    }

    @Override
    public int getEventPriority(Event event) {
        switch (event) {
            case AI_PLAYED_CARD:
                return 2;
            default:
                return 1;
        }
    }
}
