package view;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import model.cards.Suit;
import model.events.EventListener;
import model.Player;
import model.cards.Card;
import model.cards.Hand;

public class ConsoleOutput implements EventListener {
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

    public void cardChanged(Card c) {
        StringBuilder card = new StringBuilder(3);
        card.append(colors.get(c.getSuit())).append("/").append(c.getValue());
        chronology.addFirst(card.toString());

        String message = "The terrain card changed in: ".concat(chronologyToString(4));
        consoleContent.put(0, message);

        clear();
        rewrite();
    }

    public void handChanged(Player player) {
        Hand hand = player.getHand();
        String nickname = player.getNickname();
        StringBuilder sb = new StringBuilder();
        sb.append(nickname).append("'s hand is:\n0) draw 1\n");
        int i = 1;
        for (Card c : hand) {
            sb.append(i)
                    .append(")\t")
                    .append(colors.get(c.getSuit()))
                    .append(" ")
                    .append(c.getValue())
                    .append("\n");
            i += 1;
        }
        consoleContent.put(1, sb.toString());

        clear();
        rewrite();
    }

    public void warn(String message) {
        clear();
        rewrite();
        System.out.println(message);
    }

    public void playerWon(Player player) {
        clear();
        System.out.println("Well done ".concat(player.getNickname()).concat(", you won!"));
    }

    public void playerDrew(Player player) {
        chronology.addFirst("draw");

        String message = "The terrain card changed in: ".concat(chronologyToString(4));
        consoleContent.put(0, message);

        clear();
        rewrite();
    }

    @Override
    public void update(String eventType, Object data) {
        switch (eventType) {
            case "PlayerDrew":
                playerDrew((Player) data);
                break;
            case "PlayerWon":
                playerWon((Player) data);
                break;
            case "Warn":
                warn((String) data);
                break;
            case "HandChanged":
                handChanged((Player) data);
                break;
            case "CardChanged":
                cardChanged((Card) data);
                break;
        }
    }
}
