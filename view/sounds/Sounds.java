package view.sounds;

public abstract class Sounds {
    public static final Sound IN_GAME_SOUNDTRACK = new Sound("resources/soundtrack.mp3", true);

    public static final Sound BUTTON_CLICK = new Sound("resources/Audio/button_click.mp3");

    public static final void loadAll() {
        // Non fa nulla il metodo, siccome serve solo a far vedere la classe così che i fields si caricano
    }
}
