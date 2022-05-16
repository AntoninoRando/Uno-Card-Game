import java.util.List;
import java.util.ArrayList;

public class GameManager {
    private static Card terrainCard;
    private static int turns;

    public static Card getTerrainCard() {
        return terrainCard;
    }

    public static void setTerrainCard(Card card) {
        terrainCard = card;
    }

    public static int getTurns() {
        return turns;
    }

    public static void main(String[] args) {
        List<Card> smallCardSet = new ArrayList<Card>();
        for (int i = 1; i <= 9; i++) {
            for (Suit suit : Suit.values())
                smallCardSet.add(new Card(suit, i));
        }

        Deck smallDeck = new Deck(smallCardSet);
        smallDeck.shuffle();

        GameManager.setTerrainCard(smallDeck.remove(0));

        Player p1 = new Player("Human",
                smallDeck.remove(0), smallDeck.remove(0), smallDeck.remove(0), smallDeck.remove(0),
                smallDeck.remove(0));

        HumanController controllerP1 = new HumanController(p1);

        // Before playing
        System.out.println(p1.getHand().toString());
        System.out.println(GameManager.getTerrainCard().toString());

        controllerP1.playCard(0);
        
        // After playing
        System.out.println(p1.getHand().toString());
        System.out.println(GameManager.getTerrainCard().toString());
    }
}