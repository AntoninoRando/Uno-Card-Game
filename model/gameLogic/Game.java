package model.gameLogic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Predicate;
import java.util.stream.Stream;

/* --- Mine ------------------------------- */

import events.EventListener;
import events.Event;

import model.CUModel;
import model.data.CardBuilder;
import model.data.UserData;
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
    }

    /* --- Fields ----------------------------- */

    private Player[] players;
    private Card terrainCard;
    private final int firstHandSize = 1;
    private CardGroup deck = new CardGroup(CardBuilder.getCards("resources/Cards/Small.json"));
    private CardGroup discardPile;
    private Player[] turnOrder;
    private int turn; // current turn
    private final Predicate<Card> playCondition = card -> {
        Suit aS = terrainCard.getSuit();
        Suit bS = card.getSuit();
        return aS == bS || terrainCard.getValue() == card.getValue() || aS == Suit.WILD || bS == Suit.WILD;
    };
    private final Predicate<Player> winCondition = player -> player.getHand().isEmpty();
    private long timeStart;
    private boolean isInterrupted;
    private boolean running;

    /* ---.--- Getters and Setters ------------ */

    public Player[] getPlayers() {
        return players;
    }

    public Card getTerrainCard() {
        return terrainCard;
    }

    public void setTerrainCard(Card terrainCard) {
        this.terrainCard = terrainCard;
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

    public Predicate<Card> getPlayCondition() {
        return playCondition;
    }

    public int getTurn() {
        return turn;
    }

    public boolean isRunning() {
        return running;
    }

    /* --- Body ------------------------------- */

    public void advanceTurn(int ahead) {
        turn = (turn + ahead) % countPlayers();
    }

    public Player getNextPlayer() {
        return turnOrder[(turn + 1) % countPlayers()];
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

    public boolean isPlayable(Card card) {
        return playCondition.test(card);
    }

    public boolean didPlayerWin(Player player) {
        return winCondition.test(player);
    }

    /* -------------------- */

    private GameState state;

    public void changeState(GameState state) {
        this.state = state;
    }

    public void setupGame(Player[] players) {
        this.players = players;
        turnOrder = Arrays.copyOf(players, players.length);

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
        CUModel.communicate(Event.GAME_READY, data);

        // First card
        Actions.shuffleDeck();
        Card firstCard = Actions.takeFromDeck();
        Actions.changeCurrentCard(firstCard);
        CUModel.communicate(Event.CARD_CHANGE, firstCard.getData()); // Notify

        // Give cards to players
        for (Player p : Game.getInstance().getPlayers())
            Actions.dealFromDeck(p, firstHandSize);
        CUModel.communicate(Event.GAME_START, null); // Notify

        this.isInterrupted = false;
        this.running = true;
    }

    public void play() {
        setupFirstTurn();
        timeStart = System.currentTimeMillis();

        while (!isInterrupted && !didPlayerWin(getCurrentPlayer()))
            state.resolve();

        endAndReset(isInterrupted);
    }

    // TODO aggiungere che non conta il vincitore se si interrompe il gioco
    public void endAndReset(boolean interrupted) {
        if (!isInterrupted) {
            Player winner = Game.getInstance().getCurrentPlayer();
            boolean humanWon = !(winner instanceof GameAI);
            int xpEarned = (int) ((System.currentTimeMillis() - timeStart) / 60000F) + (humanWon ? 7 : 0);

            UserData.addXp(xpEarned);
            UserData.addGamePlayed(humanWon);

            HashMap<String, Object> data = UserData.wrapData();
            data.put("xp-earned", xpEarned);
            CUModel.communicate(Event.INFO_CHANGE, data);
            CUModel.communicate(Event.PLAYER_WON, winner.getData());
        }

        // Reset everything
        for (Player player : players)
            player.getHand().clear();
        instance = null;
    }

    /**
     * Interrupts this game if it is running, otherwise it'll do nothing.
     */
    public void interruptGame() {
        if (!isRunning())
            return;
        isInterrupted = true;
    }
}