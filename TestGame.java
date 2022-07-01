import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import controller.Controller;
import controller.HumanController;
import model.Game;
import model.MainLoop;
import model.Player;
import model.cards.Card;
import model.cards.Deck;
import model.cards.Suit;

public class TestGame {
    public static void main(String[] args) {
        // Creating the deck and shuffling it
        List<Card> smallCardSet = new ArrayList<Card>();
        for (int i = 1; i <= 10; i++) 
            smallCardSet.add(new Card(Suit.YELLOW, i));
        Deck smallDeck = new Deck(smallCardSet);

        // New players with their controller
        Player p1 = new Player("Antonino");
        Player p2 = new Player("Bot Giovanni");
        Player p3 = new Player("Bot Luca");

        Controller c1 = new HumanController();
        p1.setController(c1);

        TreeMap<Integer, Player> players = new TreeMap<>();
        players.put(1, p1);
        players.put(2, p2);
        players.put(3, p3);

        Game gameFor3 = new Game(players, smallDeck);
        MainLoop.getInstance().play(gameFor3);
    }
}
