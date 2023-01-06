package model.gameLogic;

import java.io.IOException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.json.simple.parser.ParseException;

import events.EventListener;
import events.EventType;
import model.CUModel;

/* --- Mine ------------------------------- */

import model.data.CardsInfo;
import model.gameEntities.GameAI;
import model.gameEntities.Player;
import model.gameObjects.*;

/**
 * A class representing the game state. This class does not modify itself,
 * because that is the <code>Loop</code> class job.
 */
public class Game implements EventListener {
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
    private long timeStart;

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

    void restoreTurnOrder() {
        turnOrder = defaultTurnOrder();
    }

    void restorePlayCondition() {
        playCondition = defaultPlayCondition();
    }

    void restoreWinCondition() {
        winCondition = defaultWinCondition();
    }

    /* -------------------- */

    private GameState state;

    public void changeState(GameState state) {
        this.state = state;
    }

    public void resolveTurn() {
        CUModel.getInstance().communicate(EventType.TURN_START, getCurrentPlayer().getData());
        state.resolveTurn();
    }

    public void nextTurn() {
        CUModel.getInstance().communicate(EventType.TURN_END, getCurrentPlayer().getData());
        setTurn(getNextPlayer());

        if (getCurrentPlayer() instanceof GameAI)
            changeState(AITurn.getInstance(getCurrentPlayer().getNickame()));
        else
            changeState(UserTurn.getInstance());
    }

    public void setupGame(Player[] players) {
        setPlayers(players);
        restoreTurnOrder();

        Player firstPlayer = turnOrder[turn];
        GameState initialState = firstPlayer instanceof GameAI ? AITurn.getInstance(firstPlayer.getNickame())
                : UserTurn.getInstance();
        for (Player player : getPlayers()) {
            if (player instanceof GameAI) 
                AITurn.getInstance(player.getNickame()).setContext(this, (GameAI) player);
            else
                UserTurn.getInstance().setContext(this, player);
        }
        changeState(initialState);
    }

    private void setupFirstTurn() {
        // Notify
        Player[] players = Game.getInstance().getPlayers();
        HashMap<String, Object> data = new HashMap<>();
        data.put("all-nicknames", Stream.of(players).map(Player::getNickame).toArray(String[]::new));
        data.put("all-icons", Stream.of(players).map(Player::getIcon).toArray(String[]::new));
        data.put("all-hand-sizes", Stream.of(players).mapToInt(p -> p.getHand().size()).toArray());
        CUModel.getInstance().communicate(EventType.GAME_READY, data);

        // First card
        Actions.shuffleDeck();
        Card firstCard = Actions.takeFromDeck();
        Actions.changeCurrentCard(firstCard);
        // Notify
        data.clear();
        data.put("card-tag", firstCard.getTag());
        CUModel.getInstance().communicate(EventType.CARD_CHANGE, data);

        // Give cards to players
        for (Player p : Game.getInstance().getPlayers())
            Actions.dealFromDeck(p, Game.getInstance().getFirstHandSize());
        // Notify
        CUModel.getInstance().communicate(EventType.GAME_START, null);
    }

    public void play() {
        setupFirstTurn();
        timeStart = System.currentTimeMillis();

        while (!isOver()) { // TODO && !isPaused) {
            resolveTurn();

            if (didPlayerWin(getCurrentPlayer()))
                end(false);
            else
                nextTurn();
        }
    }

    public void end(boolean interrupted) {
        isOver = true;

        Player winner = Game.getInstance().getCurrentPlayer();
        int xpEarned = (int) ((System.currentTimeMillis() - timeStart) / 60000F);
        // PlayerData.addXp(xpEarned);

        if (!interrupted) {
            // boolean humanWon = !(Game.getInstance().getCurrentPlayer() instanceof
            // GameAI);
            // if (humanWon) {
            // PlayerData.addXp(5);
            // xpEarned += 5;
            // }
            // PlayerData.addGamePlayed(humanWon);
            // // Notify
            // events.notify(EventType.PLAYER_WON, winner.getData());
            // Info.events.notify(EventType.XP_EARNED, xpEarned);
        }

        CUModel.getInstance().communicate(EventType.RESET, null);
        reset();
    }
}