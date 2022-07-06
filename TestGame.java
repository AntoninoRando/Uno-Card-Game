import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import controller.Controller;
import controller.HumanController;

import model.Loop;
import model.Player;

import model.cards.Card;
import model.cards.Deck;
import model.cards.Suit;

import view.ConsoleOutput;

public class TestGame {
    public static void main(String[] args) {
        /* STANDARD DECK */
        /* ------------- */
        List<Card> standardSet = new ArrayList<Card>(108);
        for (Suit color : Suit.values()) {
            if (color == Suit.WILD) {
                for (int i = 1; i < 9; i++) 
                    standardSet.add(new Card(color, 0));
                continue;
            }
            for (int i = 1; i < 10; i++) {
                standardSet.add(new Card(color, i));
                standardSet.add(new Card(color, i));
            }
            standardSet.add(new Card(color, 0));
            standardSet.add(new Card(color, 0));
        }

        Deck standardDeck = new Deck(standardSet);

        // New players with their controller
        Player p1 = new Player("Antonino", true);
        Player p2 = new Player("Bot Giovanni", false);
        Player p3 = new Player("Bot Luca", false);

        Controller c1 = new HumanController();
        c1.setSource(p1);

        TreeMap<Integer, Player> players = new TreeMap<>();
        players.put(0, p1);
        players.put(1, p2);
        players.put(2, p3);


        ConsoleOutput displayer = ConsoleOutput.getInstance();
        String[] listening = new String[] { "playerDrew", "playerWon", "warning", "turnStart", "cardPlayed" };
        try {
            Loop match = Loop.getInstance();
            match.setupView(displayer, listening);
            match.setupGame(players, standardDeck, c1);
            match.play();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
