package view.animations;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public abstract class ResetTranslate {
    public static void resetTranslate(Node node) {
        TranslateTransition reset = new TranslateTransition(Duration.millis(300.0), node);
        reset.setByX(-node.getTranslateX()); // correspond to setTranslateX(0);
        reset.setByY(-node.getTranslateY()); // correspond to setTranslateY(0);
        reset.play();
    }
}
