package view;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import model.cards.Suit;
import model.cards.Card;
import model.cards.Hand;
import model.listeners.EndListener;
import model.listeners.HandListener;
import model.listeners.InvalidActionListener;
import model.listeners.TerrainListener;

public class ConsoleOutput implements TerrainListener, HandListener, InvalidActionListener, EndListener {
    /* IMPLEMENTING SINGLETON PATTERN */
    /* ------------------------------ */
    private static ConsoleOutput instance;

    public static ConsoleOutput getInstance() {
        if (instance == null)
            instance = new ConsoleOutput();
        return instance;
    }

    private ConsoleOutput() {
    }

    /* CORE */
    /* --------------- */
    Map<Suit, String> colors = Stream.of(new Object[][] {
            { Suit.GREEN, "\u001B[32m green\u001B[0m" },
            { Suit.RED, "\u001B[31m red\u001B[0m" },
            { Suit.YELLOW, "\u001B[33m yellow\u001B[0m" },
            { Suit.BLUE, "\u001B[34m blue\u001B[0m" }
    }).collect(Collectors.toMap(p -> (Suit) p[0], p -> (String) p[1]));

    @Override
    public void cardChanged(Card c) {
        System.out.print("The terrain card changed in: ");
        System.out.println(colors.get(c.getSuit()) + " - " + c.getValue());
    }

    @Override
    public void handChanged(Hand hand, String nickname) {
        System.out.println(nickname + "'s hand is:\n0) draw 1");

        int i = 1;
        for (Card c : hand) {
            System.out.println(i + ") " + colors.get(c.getSuit()) + " " + c.getValue());
            i += 1;
        }
    }

    @Override
    public void warn(String message) {
        System.out.println(message);
    }

    @Override
    public void playerWon(String nickname) {
        System.out.println("Well done " + nickname + ", you won!");
    }
}
