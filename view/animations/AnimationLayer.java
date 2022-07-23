package view.animations;

import javafx.application.Platform;
import javafx.scene.Group;

public class AnimationLayer extends Group {
    /* SINGLETON */
    /* --------- */
    private static AnimationLayer instance;

    public static AnimationLayer getInstance() {
        if (instance == null)
            instance = new AnimationLayer();
        return instance;
    }

    private AnimationLayer() {
    }

    protected void addAnimationGroup(Group animation) {
        Platform.runLater(() -> {
            getChildren().add(animation);
        });
    }

    protected void removeAnimationGroup(Group animation) {
        Platform.runLater(() -> {
            getChildren().remove(animation);
        });
    }
}
