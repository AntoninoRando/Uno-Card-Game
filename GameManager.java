import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This class manage the game. A game is an istance of GameManager.
 */
public class GameManager {
    // VARIABLES
    private Deck drawingDeck;
    private CardGroup discardPile = new CardGroup();
    private Card terrainCard;
    private int turns;

    // We use LinkedList to define the order in which players play.
    private List<Controller> controllers = new LinkedList<Controller>();

    // CONSTRUCTORS
    public GameManager(Deck drawingDeck, List<Controller> controllers) {
        this.drawingDeck = (Deck) drawingDeck.shuffle();
        terrainCard = this.drawingDeck.remove(0);
        turns = 1;

        this.controllers = controllers;
    }

    // METHODS
    public void playTurns() {
        for (Controller controller : controllers)
            controller.playCardsFromInput(this);
        turns++;
    }

    public void playGame() {
        while (!controllers.isEmpty()) {
            playTurns();
        }
    }

    public boolean reShuffle() {
        boolean hasChanghed = drawingDeck.addAll(discardPile.shuffle().getCards());
        discardPile.clear();
        return hasChanghed;
    }

    public Card drawFromDeck() {
        if (drawingDeck.isEmpty())
            reShuffle();
        return drawingDeck.remove(0);
    }

    public void putCard(Card card) {
        discardPile.add(terrainCard);
        terrainCard = card;
    }

    public boolean playCard(Card card) {
        if (!card.isPlayable(terrainCard))
            return false;
        
        putCard(card);
        return true;
    }

    // GETTERS AND SETTERS
    public Deck getDrawingDeck() {
        return drawingDeck;
    }

    public Card getTerrainCard() {
        return terrainCard;
    }

    public int getTurns() {
        return turns;
    }

    // MAIN
    public static void main(String[] args) {
        // Creating the deck and shuffling it
        List<Card> smallCardSet = new ArrayList<Card>();
        for (int i = 1; i <= 9; i++) {
            for (Suit suit : Suit.values())
                smallCardSet.add(new Card(suit, i));
        }
        Deck smallDeck = new Deck(smallCardSet);
        smallDeck.shuffle(); //!Shufflo perchè i giocatori devono avere carte casuali

        // New players with their controller
        Player p1 = new Player("Antonino",
        smallDeck.remove(0), smallDeck.remove(0), smallDeck.remove(0), smallDeck.remove(0),
        smallDeck.remove(0));
        HumanController controllerP1 = new HumanController(p1);

        Player p2 = new Player("Alice",
        smallDeck.remove(0), smallDeck.remove(0), smallDeck.remove(0), smallDeck.remove(0),
        smallDeck.remove(0));
        HumanController controllerP2 = new HumanController(p2);

        GameManager g1 = new GameManager(smallDeck, List.of(controllerP1, controllerP2));
        g1.playGame();
    }
}