package view.animations;

import java.util.function.Supplier;

public abstract class Animations {
    public static final Supplier<Animation> UNO_TEXT = () -> new Animation("resources\\AnimazioneUno");

    public static final Supplier<Animation> BLOCK_TURN = () -> new Animation("resources\\BlockTurn");

    public static final Supplier<Animation> CARD_PLAYED = () -> new Animation("resources\\PlayCard");

    public static final Supplier<Animation> FOCUS_PLAYER = () -> new Animation("resources\\FocusTurn");

    public static final Supplier<Animation> NEW_GAME = () -> new Animation("resources\\NewGame");
}
