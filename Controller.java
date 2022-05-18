public abstract class Controller {
    protected Player bringer;
    protected GameManager game;

    // CONSTRUCTORS !Non so se vada fatto

    // METHODS
    public abstract void playCardsFromInput();

    // GETTERS AND SETTERS
    public GameManager getGame() {
        return game;
    }

    public void setGame(GameManager game) {
        this.game = game;
    }
}