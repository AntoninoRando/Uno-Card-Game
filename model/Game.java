package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;

import model.cards.Card;
import model.cards.CardGroup;
import model.cards.Deck;
import model.cards.Suit;
import model.effects.Effect;
import model.effects.EffectBuilder;

/**
 * This class represents a game frame. Think of this class as a node
 * in a finite state machine. The transitions between nodes are
 * performed by the MainLoop. Any destination node is still represent by this
 * class, but with different values in its fields.
 * <hr>
 * Following this analogy, the Game can not modify itself, but it can only
 * return its values to the requesters. The MainLoop will modify the Game's
 * fileds.
 */
public class Game {
    /* SINGLETON PATTERN */
    /* ----------------- */
    private static Game instance;

    public static Game getInstance() {
        if (instance == null)
            instance = new Game();
        return instance;
    }

    private Game() {
        discardPile = new CardGroup();
        playCondition = defaultPlayCondition;
        winCondition = defaultWinCondition;
        nextPlayer = defaultNextPlayer;
    }

    static void reset() {
        instance = null;
    }

    /* FIELDS */
    /* ------ */
    private TreeMap<Integer, Player> players;

    private int turn;

    protected Deck deck = standardDeck();
    private Deck standardDeck() {
        List<Card> standardSet = new ArrayList<Card>(108);
        Effect blockTurn = new EffectBuilder().directTargetToFollowing(1).skipTargetTurn().build();
        Effect draw2 = new EffectBuilder().directTargetToFollowing(1).targetDraws(2).skipTargetTurn().build();
        Effect reverseTurn = new EffectBuilder().reverseTurnOrder().build();
        Effect pickColor = new EffectBuilder().selectOneCardOf(new Card(Suit.RED, -5), new Card(Suit.BLUE, -5),
                new Card(Suit.YELLOW, -5), new Card(Suit.GREEN, -5)).transformIntoTarget().build();
        Effect draw4 = new EffectBuilder().directTargetToFollowing(1).targetDraws(4).skipTargetTurn()
                .selectOneCardOf(new Card(Suit.RED, -4), new Card(Suit.BLUE, -4),
                        new Card(Suit.YELLOW, -4), new Card(Suit.GREEN, -4))
                .transformIntoTarget().build();

        for (Suit color : Suit.values()) {
            if (color == Suit.WILD)
                continue;

            for (int i = 1; i < 10; i++) {
                standardSet.add(new Card(color, i));
                standardSet.add(new Card(color, i));
            }

            standardSet.add(new Card(color, 0));
            standardSet.add(new Card(color, 0));
            standardSet.add(new Card(color, -1, blockTurn));
            standardSet.add(new Card(color, -1, blockTurn));
            standardSet.add(new Card(color, -2, draw2));
            standardSet.add(new Card(color, -2, draw2));
            standardSet.add(new Card(color, -3, reverseTurn));
            standardSet.add(new Card(color, -3, reverseTurn));
            standardSet.add(new Card(Suit.WILD, -5, pickColor));
            standardSet.add(new Card(Suit.WILD, -4, draw4));
        }

        return new Deck(standardSet);
    }

    protected Card terrainCard;
    protected CardGroup discardPile;

    private Predicate<Card> playCondition;
    private final Predicate<Card> defaultPlayCondition = card -> {
        Suit aS = terrainCard.getSuit();
        Suit bS = card.getSuit();
        return aS == Suit.WILD || bS == Suit.WILD || aS == bS || terrainCard.getValue() == card.getValue();
    };

    private Predicate<Player> winCondition;
    private final Predicate<Player> defaultWinCondition = player -> {
        return player.hand.isEmpty();
    };

    private Function<Player, Player> nextPlayer;
    private final Function<Player, Player> defaultNextPlayer = player -> getPlayer(getTurn(player) + 1);

    private boolean isOver;

    /* GETTERS AND SETTERS */
    /* ------------------- */
    public Player getPlayer() {
        return players.get(turn);
    }

    public Player getPlayer(int theirTurn) {
        return players.get(theirTurn % countPlayers());
    }

    public Player getNextPlayer() {
        return nextPlayer.apply(getPlayer());
    }

    public Player getNextPlayer(Player start) {
        return nextPlayer.apply(start);
    }

    public int countPlayers() {
        return players.size();
    }

    public Collection<Player> getPlayers() {
        return players.values();
    }

    public void setPlayers(TreeMap<Integer, Player> players) {
        this.players = players;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int i) {
        turn = i % countPlayers();
    }

    public void setTurn(Player p) {
        turn = getTurn(p);
    }

    public int getTurn(Player p) {
        return players.entrySet().stream()
                .filter(entry -> p.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst().get();
    }

    public boolean isOver() {
        return isOver;
    }

    public boolean isPlayable(Card c) {
        return playCondition.test(c);
    }

    public void setPlayConditon(Predicate<Card> newCondition) {
        playCondition = newCondition;
    }

    public void restorePlayCondition() {
        playCondition = defaultPlayCondition;
    }

    public boolean didPlayerWin(Player p) {
        return winCondition.test(p);
    }

    public void setWinCondition(Predicate<Player> newCondition) {
        winCondition = newCondition;
    }

    public void restoreWinCondition() {
        winCondition = defaultWinCondition;
    }

    public Function<Player, Player> getNextPlayerEvaluator() {
        return nextPlayer;
    }

    public void setNextPlayerEvaluator(Function<Player, Player> newEvaluator) {
        nextPlayer = newEvaluator;
    }

    public void restoreNextPlyerEvaluator() {
        nextPlayer = defaultNextPlayer;
    }

    public void end() {
        isOver = true;
    }
}