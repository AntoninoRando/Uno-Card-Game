package view.animations;

import java.util.HashMap;
import java.util.function.Supplier;

import javafx.scene.image.ImageView;

public abstract class Animations {
    public static final Supplier<Animation> UNO_TEXT = () -> new Animation("resources\\AnimazioneUno");

    public static final Supplier<Animation> BLOCK_TURN = () -> new Animation("resources\\BlockTurn");

    public static final Supplier<Animation> CARD_PLAYED = () -> new Animation("resources\\PlayCard");

    public static final Supplier<Animation> FOCU_PLAYER = () -> new Animation("resources\\Focus");

    protected static HashMap<String, ImageView[]> imagesLoaded = new HashMap<String, ImageView[]>();
}
