package view;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Consumer;
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
        chronology = new LinkedList<>();
        listening = new HashMap<>();
        colors = Stream.of(new Object[][] {
                { Suit.GREEN, "\u001B[32m green\u001B[0m" },
                { Suit.RED, "\u001B[31m red\u001B[0m" },
                { Suit.YELLOW, "\u001B[33m yellow\u001B[0m" },
                { Suit.BLUE, "\u001B[34m blue\u001B[0m" },
                { Suit.WILD, "\u001B[36m wild\u001B[0m" }
        }).collect(Collectors.toMap(p -> (Suit) p[0], p -> (String) p[1]));

        // listening.put("PlayerDrew", (data) -> playerDrew((Player) data));
        // listening.put("PlayerWon", (data) -> playerWon((Player) data));
        // listening.put("Warn", (data) -> warning((String) data));
        // listening.put("HandChanged", (data) -> handChanged((Player) data));
        // listening.put("CardChanged", (data) -> cardChanged((Card) data));

        listening.put("playerDrew", (data) -> playerDrew((Player) data));
        listening.put("PlayerWon", (data) -> playerWon((Player) data));
        listening.put("warning", (data) -> warning((String) data));
        listening.put("turnStart", (data) -> handChanged((Player) data));
        listening.put("cardPlayed", (data) -> cardChanged((Card) data));
    }

    /* CORE */
    /* --------------- */
    private TreeMap<Integer, String> consoleContent;
    private LinkedList<String> chronology;
    private Map<String, Consumer<Object>> listening;
    private Map<Suit, String> colors;

    public Set<String> getEventsListening() {
        return listening.keySet();
    }

    private void clear() {
        System.out.print("\033[H\033[2J");
    }

    private void write() {
        consoleContent.values().forEach(System.out::println);
    }

    private void updateView() {
        clear();
        write();
    }

    private String chronologyToString(int pretty) {
        if (pretty > chronology.size())
            pretty = chronology.size();
        return String.join(" <=== ", chronology.subList(0, pretty));
    }

    public void cardChanged(Card c) {
        StringBuilder card = new StringBuilder(3);
        card.append(colors.get(c.getSuit())).append("/").append(c.getValue());
        chronology.addFirst(card.toString());

        String message = "The terrain card changed in: ".concat(chronologyToString(4));
        consoleContent.put(0, message);

        updateView();
    }

    public void handChanged(Player player) {
        if (!player.isHuman())
            return;
        Hand hand = player.getHand();
        String nickname = player.getNickname();
        StringBuilder sb = new StringBuilder();
        sb.append(nickname).append("'s hand is:\n");
        int i = 0;
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

        updateView();
    }

    public void warning(String message) {
        consoleContent.put(2, message);
        updateView();
    }

    public void playerWon(Player player) {
        clear();
        System.out.println("Well done ".concat(player.getNickname()).concat(", you won!"));
    }

    public void playerDrew(Player player) {
        chronology.addFirst(player.getNickname() + " drew");

        String message = "The terrain card changed in: ".concat(chronologyToString(4));
        consoleContent.put(0, message);

        updateView();
    }

    @Override
    public void update(String eventType, Object data) {
        listening.getOrDefault(eventType, (__) -> {
            throw new Error("The ConsoleOutput was listening for \"" + eventType + "\" but it can't handle this event");
        }).accept(data);
    }
}
