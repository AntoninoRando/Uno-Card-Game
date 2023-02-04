package model.players;

/**
 * An AI name. To get the actual <code>GameAI</code> instance, use the
 * <code>get()</code> method.
 */
public enum Enemy {
    Viego("pirate", "easy"),
    Jinx("blood", "easy"),
    Zoe("queen", "easy"),
    Xayah("night", "easy");

    private Player player;

    /**
     * Instantiates a new <code>GameAI</code> object with this nickname, the given
     * icon and of the given type.
     * 
     * @param icon The icon of the player.
     * @param type The class type of the <code>GameAI</code>.
     */
    private Enemy(String icon, String type) {
        if (type.equals("easy"))
            player = new EasyAI(icon, this.toString());
    }

    /**
     * Returns the <code>GameAI</code> object with this nickname.
     * 
     * @return The <code>GameAI</code> object with this nickname.
     */
    public Player get() {
        return player;
    }
}
