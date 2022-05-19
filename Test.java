import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import CardsTools.Card;
import CardsTools.Deck;
import CardsTools.Suit;
import Controllers.HumanController;
import Controllers.AIController;
import EffectsTools.Effect;
import GameTools.GameManager;
import GameTools.Player;

public class Test {
    // MAIN
    public static void main(String[] args) {
        // Creating the deck and shuffling it
        List<Card> smallCardSet = new ArrayList<Card>();
        for (int i = 1; i <= 9; i++) {
            for (Suit suit : Suit.values()) {
                if (suit == Suit.WILD) 
                    continue;
                smallCardSet.add(new Card(suit, i));
            }
            // Special card
            Card draw2 = new Card(Suit.WILD, 0);
            Map<String, String> draw2e = new HashMap<String,String>() ;
            draw2e.put("play", "draw(2, me)");
            draw2.addEffect(new Effect(draw2e));
            smallCardSet.add(draw2);
        }

        Deck smallDeck = new Deck(smallCardSet);

        // New players with their controller
        Player p1 = new Player("Antonino");
        HumanController controllerP1 = new HumanController(p1);

        Player p2 = new Player("Bot Giovanni");
        AIController bot1 = new AIController(p2);

        Player p3 = new Player("Bot Luca");
        AIController bot2 = new AIController(p3);

        GameManager g1 = new GameManager(smallDeck, controllerP1, bot1, bot2);

        g1.playGame();
    }
}
