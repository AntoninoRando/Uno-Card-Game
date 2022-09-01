package view.sounds;

import java.util.function.Supplier;

public abstract class Sounds {
    public static final Sound IN_GAME_SOUNDTRACK = new Sound("resources/soundtrack.mp3");

    public static final Supplier<Sound> BUTTON_CLICK = () -> new Sound("resources/Audio/button_click.mp3", 1);
}
