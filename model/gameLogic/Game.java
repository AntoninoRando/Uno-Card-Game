package model.gameLogic;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/* --- Mine ------------------------------- */

import events.EventListener;
import events.Event;

import model.CUModel;
import model.data.CardBuilder;
import model.data.UserData;
import model.gameEntities.Enemies;
import model.gameEntities.GameAI;
import model.gameEntities.Player;
import model.gameObjects.*;

/**
 * Gathers all game info and contains the logic to execute a match.
 */
public class Game extends Thread implements EventListener {
    /* --- Singleton -------------------------- */

    private static Game instance;

    public static void createInstance() {
        if (instance != null)
            return;
        instance = new Game();
    }

    private Game() {
        discardPile = new LinkedList<>();
        deck = new LinkedList<>(CardBuilder.getCards("resources/Cards/Small.json"));
        playCondition = card -> {
            Suit aS = terrainCard.getSuit();
            Suit bS = card.getSuit();
            return aS == bS || terrainCard.getValue() == card.getValue() || aS == Suit.WILD || bS == Suit.WILD;
        };
        winCondition = player -> player.getHand().isEmpty();
        players = new Player[] { new Player(), Enemies.JINX, Enemies.VIEGO, Enemies.XAYAH, Enemies.ZOE };
    }

    /* --- Fields ----------------------------- */

    private Player[] players;
    private Card terrainCard;
    private final int firstHandSize = 7;
    private List<Card> deck;
    private List<Card> discardPile;
    private int turn; // current turn
    private Predicate<Card> playCondition;
    private Predicate<Player> winCondition;
    private long timeStart;
    private boolean interrupted;
    private boolean running;
    private GameState state; // Context

    /* ---.--- Getters and Setters ------------ */

    public static boolean isInstantiated() {
        return instance != null;
    }

    public static Player[] getPlayers() {
        return instance.players;
    }

    public static Card getTerrainCard() {
        return instance.terrainCard;
    }

    public static void setTerrainCard(Card newCard) {
        instance.terrainCard = newCard;
    }

    public static List<Card> getDeck() {
        return instance.deck;
    }

    public static List<Card> getDiscardPile() {
        return instance.discardPile;
    }

    public static void setTurnOrder(Player[] newOrder) {
        instance.players = newOrder;
    }

    public static Predicate<Card> getPlayCondition() {
        return instance.playCondition;
    }

    public static int getTurn() {
        return instance.turn;
    }

    public static void setTurn(int newTurn) {
        instance.turn = newTurn;
    }

    public static boolean isBlocked() {
        return instance.interrupted;
    }

    public static boolean isRunning() {
        return instance.running;
    }

    /* --- Body ------------------------------- */

    public static Player getCurrentPlayer() {
        return instance.players[instance.turn];
    }

    public static int countPlayers() {
        return instance.players.length;
    }

    public static boolean isPlayable(Card card) {
        return instance.playCondition.test(card);
    }

    public static boolean didPlayerWin(Player player) {
        return instance.winCondition.test(player);
    }

    /**
     * Interrupts this game if it is running, otherwise it'll do nothing.
     */
    public static void interruptGame() {
        if (!isRunning())
            return;
        instance.interrupted = true;
    }

    /* --- State ------------------------------ */

    public static void changeState(GameState newState) {
        instance.state = newState;
    }

    /* --- Game Loop -------------------------- */

    public static void setupGame() {
        createInstance();

        try {
            GameAI firstPlayer = (GameAI) getCurrentPlayer();
            AITurn initialState = new AITurn();
            initialState.setContext(firstPlayer);
            changeState(initialState);
        } catch (ClassCastException e) {
            UserTurn initialState = UserTurn.getInstance();
            initialState.setContext(getCurrentPlayer());
            changeState(initialState);
        }
    }

    private static void setupFirstTurn() {
        // Notify
        HashMap<String, Object> data = new HashMap<>();

        data.put("all-nicknames", Stream.of(getPlayers()).map(Player::getNickame).toArray(String[]::new));
        data.put("all-icons", Stream.of(getPlayers()).map(Player::getIcon).toArray(String[]::new));
        CUModel.communicate(Event.GAME_READY, data);

        // First card
        Actions.shuffleDeck();
        Card firstCard = Actions.takeFromDeck();
        Actions.changeCurrentCard(firstCard);
        CUModel.communicate(Event.CARD_CHANGE, firstCard.getData()); // Notify

        // Give cards to players
        for (Player p : getPlayers())
            Actions.dealFromDeck(p, instance.firstHandSize);
        CUModel.communicate(Event.GAME_START, null); // Notify

        instance.interrupted = false;
        instance.running = true;
    }

    public static void play() {
        setupGame();
        setupFirstTurn();
        instance.timeStart = System.currentTimeMillis();

        while (!isBlocked() && !didPlayerWin(getCurrentPlayer()))
            instance.state.resolve();

        endAndReset(isBlocked());
    }

    private static void endAndReset(boolean interrupted) {
        if (!interrupted) {
            Player winner = getCurrentPlayer();
            boolean humanWon = !(winner instanceof GameAI);
            int xpEarned = (int) ((System.currentTimeMillis() - instance.timeStart) / 60000F) + (humanWon ? 3 : 0);

            UserData.addXp(xpEarned);
            UserData.addGamePlayed(humanWon);

            HashMap<String, Object> data = UserData.wrapData();
            data.put("xp-earned", xpEarned);
            CUModel.communicate(Event.INFO_CHANGE, data);
            CUModel.communicate(Event.PLAYER_WON, winner.getData());
        }

        // Reset everything
        for (Player player : getPlayers())
            player.getHand().clear();
        
        instance = null;
    }
}