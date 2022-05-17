import java.util.List;
import java.util.ArrayList;

/**
 * This class is one game.
 */
public class GameManager {
    private Deck drawingDeck;
    private Card terrainCard;
    private int turns;

    public GameManager(Deck drawingDeck) {
        this.drawingDeck = drawingDeck.shuffle();
        terrainCard = this.drawingDeck.remove(0);
        turns = 1;
    }

    public Deck getDrawingDeck() {
        return drawingDeck;
    }

    public Card getTerrainCard() {
        return terrainCard;
    }

    public void setTerrainCard(Card card) {
        terrainCard = card;
    }

    public int getTurns() {
        return turns;
    }

    public static void main(String[] args) {
        // Creating the deck and shuffling it
        List<Card> smallCardSet = new ArrayList<Card>();
        for (int i = 1; i <= 9; i++) {
            for (Suit suit : Suit.values())
                smallCardSet.add(new Card(suit, i));
        }
        Deck smallDeck = new Deck(smallCardSet);

        // Set the first card
        GameManager g1 = new GameManager(smallDeck);

        // New player with its controller
        Player p1 = new Player("Human",
                smallDeck.remove(0), smallDeck.remove(0), smallDeck.remove(0), smallDeck.remove(0),
                smallDeck.remove(0));

        HumanController controllerP1 = new HumanController(p1);

        // Print before playing one card
        System.out.println(p1.getHand().toString());
        System.out.println(g1.getTerrainCard().toString());

        controllerP1.playCardsFromInput(g1);

        // Print after 
        System.out.println(p1.getHand().toString());
        System.out.println(g1.getTerrainCard().toString());
    }
}