package model.gameLogic;

import java.io.IOException;

import java.util.Arrays;
import java.util.function.Predicate;

import org.json.simple.parser.ParseException;

/* --- Mine ------------------------------- */

import model.data.CardsInfo;
import model.gameObjects.*;

/**
 * A class representing the game state. This class does not modify itself,
 * because that is the <code>Loop</code> class job.
 */
public class Game {
    /* --- Singleton -------------------------- */

    private static Game instance;

    public static Game getInstance() {
        if (instance == null)
            instance = new Game();
        return instance;
    }

    private Game() {
        discardPile = new CardGroup();
        restorePlayCondition();
        restoreWinCondition();
    }

    /* --- Fields ----------------------------- */

    private Player[] players;
    private Card terrainCard;
    private final int firstHandSize = 7;
    private CardGroup deck = standardDeck();
    private CardGroup discardPile;
    private Player[] turnOrder;
    private int turn; // current turn
    private Predicate<Card> playCondition;
    private Predicate<Player> winCondition;
    private boolean isOver;

    /* ---.--- Getters and Setters ------------ */

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public Card getTerrainCard() {
        return terrainCard;
    }

    public void setTerrainCard(Card terrainCard) {
        this.terrainCard = terrainCard;
    }

    public int getFirstHandSize() {
        return firstHandSize;
    }

    public CardGroup getDeck() {
        return deck;
    }

    public CardGroup getDiscardPile() {
        return discardPile;
    }

    public Player[] getTurnOrder() {
        return turnOrder;
    }

    public void setTurnOrder(Player[] newOrder) {
        turnOrder = newOrder;
    }

    public int getTurn() {
        return turn;
    }

    public void setPlayConditon(Predicate<Card> newCondition) {
        playCondition = newCondition;
    }

    public void setWinCondition(Predicate<Player> newCondition) {
        winCondition = newCondition;
    }

    public boolean isOver() {
        return isOver;
    }

    /* ---.--- Default values ----------------- */

    /**
     * 
     * @return The default turn order, that is the list of players as it was
     *         originally created.
     */
    private Player[] defaultTurnOrder() {
        return players;
    }

    private CardGroup standardDeck() {
        try {
            return CardsInfo.load("Standard");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Predicate<Card> defaultPlayCondition() {
        return card -> {
            Suit aS = terrainCard.getSuit();
            Suit bS = card.getSuit();
            return aS == Suit.WILD || bS == Suit.WILD || aS == bS || terrainCard.getValue() == card.getValue();
        };
    }

    private Predicate<Player> defaultWinCondition() {
        return player -> {
            return player.getHand().isEmpty();
        };
    }

    /* --- Body ------------------------------- */

    public int getNextTurn() {
        return (turn + 1) % countPlayers();
    }

    public Player getNextPlayer() {
        return turnOrder[getNextTurn()];
    }

    public Player getCurrentPlayer() {
        return turnOrder[turn];
    }

    /**
     * 
     * @param turn Their turn.
     * @return The player that plays in the given turn.
     */
    public Player getPlayerByTurn(int turn) {
        return turnOrder[turn % countPlayers()];
    }

    public int countPlayers() {
        return players.length;
    }

    public int getTurnOf(Player player) {
        return Arrays.asList(turnOrder).indexOf(player);
    }

    // TODO non so cosa faccia questo metodo... forse setuppa il turno
    public void setTurn(Player player) {
        for (int i = 0; i < countPlayers(); i++)
            if (turnOrder[i] == player)
                turn = i;
    }

    public boolean isPlayable(Card card) {
        return playCondition.test(card);
    }

    public boolean didPlayerWin(Player player) {
        return winCondition.test(player);
    }

    static void reset() {
        instance = null;
    }

    void end() {
        isOver = true;
    }

    void restoreTurnOrder() {
        turnOrder = defaultTurnOrder();
    }

    void restorePlayCondition() {
        playCondition = defaultPlayCondition();
    }

    void restoreWinCondition() {
        winCondition = defaultWinCondition();
    }

}