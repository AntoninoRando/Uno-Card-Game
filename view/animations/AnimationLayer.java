package view.animations;

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
        setVisible(false);
    }

    /* --------------------------------------- */
}
