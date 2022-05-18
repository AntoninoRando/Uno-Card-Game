public abstract class Controller {
    protected Player bringer;
    protected GameManager game;

    // CONSTRUCTORS !Non so se vada fatto

    // METHODS
    public abstract int getPlay();

    public abstract void makePlay();

    protected void drawFromDeck() {
        bringer.addCard(game.drawFromDeck());
    }

    protected void drawFromDeck(int times) {
        while (times-- > 0)
            bringer.addCard(game.drawFromDeck());
    }

    // GETTERS AND SETTERS
    public Player getBringer() {
        return bringer;
    }

    public GameManager getGame() {
        return game;
    }

    public void setGame(GameManager game) {
        this.game = game;
    }
}