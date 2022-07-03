package view;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import model.cards.Suit;
import model.cards.Card;
import model.cards.Hand;
import model.listeners.DrawListener;
import model.listeners.EndListener;
import model.listeners.HandListener;
import model.listeners.InvalidActionListener;
import model.listeners.TerrainListener;

public class ConsoleOutput implements TerrainListener, HandListener, InvalidActionListener, EndListener, DrawListener {
    /* IMPLEMENTING SINGLETON PATTERN */
    /* ------------------------------ */
    private static ConsoleOutput instance;

    public static ConsoleOutput getInstance() {
        if (instance == null)
            instance = new ConsoleOutput();
        return instance;
    }

    private ConsoleOutput() {
        consoleContent = new TreeMap<>();
    }

    /* CORE */
    /* --------------- */
    private TreeMap<Integer, String> consoleContent;
    private LinkedList<String> chronology = new LinkedList<>();

    private void clear() {
        System.out.print("\033[H\033[2J");
    }

    private void rewrite() {
        consoleContent.forEach((__, s) -> System.out.println(s));
    }

    private String chronologyToString(int pretty) {
        if (pretty > chronology.size())
            pretty = chronology.size();
        return String.join(" <=== ", chronology.subList(0, pretty));
    }

    Map<Suit, String> colors = Stream.of(new Object[][] {
            { Suit.GREEN, "\u001B[32m green\u001B[0m" },
            { Suit.RED, "\u001B[31m red\u001B[0m" },
            { Suit.YELLOW, "\u001B[33m yellow\u001B[0m" },
            { Suit.BLUE, "\u001B[34m blue\u001B[0m" },
            { Suit.WILD, "\u001B[36m wild\u001B[0m" }
    }).collect(Collectors.toMap(p -> (Suit) p[0], p -> (String) p[1]));

    @Override
    public void cardChanged(Card c) {
        StringBuilder card = new StringBuilder(3);
        card.append(colors.get(c.getSuit())).append("/").append(c.getValue());
        chronology.addFirst(card.toString());

        String message = "The terrain card changed in: ".concat(chronologyToString(4));
        consoleContent.put(0, message);

        clear();
        rewrite();
    }

    @Override
    public void handChanged(Hand hand, String nickname) {
        StringBuilder sb = new StringBuilder();
        sb.append(nickname).append("'s hand is:\n0) draw 1\n");
        int i = 1;
        for (Card c : hand) {
            sb.append(i).append(")\t").append(colors.get(c.getSuit())).append(" ").append(c.getValue());
            if (c.getValue() == 7)
                sb.append(" (effect: draw 2)");
            sb.append("\n");
            i += 1;
        }
        consoleContent.put(1, sb.toString());

        clear();
        rewrite();
    }

    @Override
    public void warn(String message) {
        clear();
        rewrite();
        System.out.println(message);
    }

    @Override
    public void playerWon(String nickname) {
        clear();
        System.out.println("Well done ".concat(nickname).concat(", you won!"));
    }

    @Override
    public void playerDrew(String nickname) {
        chronology.addFirst("draw");

        String message = "The terrain card changed in: ".concat(chronologyToString(4));
        consoleContent.put(0, message);

        clear();
        rewrite();
    }
}
