import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import CardsTools.Card;
import CardsTools.Deck;
import CardsTools.Suit;

import EffectsTools.Effect;

import GUI.GameFrame;

import GameTools.Game;
import GameTools.GameController;
import GameTools.Player;

public class TestGUI {
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
            Map<String, String> draw2e = new HashMap<String, String>();
            draw2e.put("play", "draw(2, me)");
            draw2.addEffect(new Effect(draw2e));
            smallCardSet.add(draw2);
        }

        Deck smallDeck = new Deck(smallCardSet);

        // New players with their controller
        Player p1 = new Player("Antonino");
        Player p2 = new Player("Bot Giovanni");
        Player p3 = new Player("Bot Luca");

        Game game1 = new Game(smallDeck, p1, p2, p3);
        GameController g1 = new GameController(game1, new boolean[] {false, true, true}, 0, 1, 2);

        new GameFrame(g1);
    }
}
