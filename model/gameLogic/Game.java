package model.gameLogic;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/* --- Mine ------------------------------- */

import events.Event;

import model.CUModel;
import model.cards.Card;
import model.cards.CardBuilder;
import model.cards.Suit;
import model.players.Enemies;
import model.players.GameAI;
import model.players.Player;
import model.players.UserData;

/**
 * Gathers all game info and contains the logic to execute a match.
 * <p>
 * It is not a singleton, since it is easier to think of a new match like a new
 * Game object, instead of the same game object that resets.
 */
public class Game {
    public Game() {
        discardPile = new LinkedList<>();
        deck = new LinkedList<>(CardBuilder.getCards("resources/cards/Small.json"));
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
    private List<Card> deck, discardPile;
    private int turn; // current turn
    private Predicate<Card> playCondition;
    private Predicate<Player> winCondition;
    private long timeStart;
    private boolean interrupted;
    private GameState state; // Context

    /* ---.--- Getters and Setters ------------ */

    public Player[] getPlayers() {
        return players;
    }

    public void setTurnOrder(Player[] newOrder) {
        players = newOrder;
    }

    public Predicate<Card> getPlayCondition() {
        return playCondition;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int newTurn) {
        turn = newTurn;
    }

    public boolean isBlocked() {
        return interrupted;
    }

    public Player getCurrentPlayer() {
        return players[turn];
    }

    public void block() {
        interrupted = true;
    }

    /* --- State ------------------------------ */

    public void changeState(GameState newState) {
        state = newState;
    }

    /* --- Game Loop -------------------------- */

    public void setupGame() {
        try {
            GameAI firstPlayer = (GameAI) getCurrentPlayer();
            AITurn initialState = new AITurn();
            initialState.setContext(firstPlayer, this);
            changeState(initialState);
        } catch (ClassCastException e) {
            UserTurn initialState = UserTurn.getInstance();
            initialState.setContext(getCurrentPlayer(), this);
            changeState(initialState);
        }

        // Notify
        HashMap<String, Object> data = new HashMap<>();

        data.put("all-nicknames", Stream.of(getPlayers()).map(Player::getNickame).toArray(String[]::new));
        data.put("all-icons", Stream.of(getPlayers()).map(Player::getIcon).toArray(String[]::new));
        notifyToCU(Event.GAME_READY, data);
    }

    private void setupFirstTurn() {
        // First card
        shuffleDeck();
        Card firstCard = takeFromDeck();
        changeCurrentCard(firstCard);
        notifyToCU(Event.CARD_CHANGE, firstCard.getData()); // Notify

        // Give cards to players
        for (Player player : players) {
            player.getHand().clear();
            dealFromDeck(player, firstHandSize);
        }
        notifyToCU(Event.GAME_START, null); // Notify
    }

    public void play() {
        setupGame();
        setupFirstTurn();
        timeStart = System.currentTimeMillis();

        while (!isBlocked() && !winCondition.test(getCurrentPlayer()))
            state.resolve();

        if (!isBlocked())
            end();
    }

    private void end() {
        Player winner = getCurrentPlayer();
        boolean humanWon = !(winner instanceof GameAI);
        int xpEarned = (int) ((System.currentTimeMillis() - timeStart) / 60000F) + (humanWon ? 3 : 0);

        UserData.addXp(xpEarned);
        UserData.addGamePlayed(humanWon);

        HashMap<String, Object> data = UserData.wrapData();
        data.put("xp-earned", xpEarned);
        notifyToCU(Event.INFO_CHANGE, data);
        notifyToCU(Event.PLAYER_WON, winner.getData());
    }

    /* --- Body ------------------------------- */

    /**
     * Replace the current terrain card with the given card.
     * 
     * @param card The new terrain card.
     */
    public void changeCurrentCard(Card card) {
        if (terrainCard != null)
            discardPile.add(terrainCard);
        terrainCard = card;
    }

    /**
     * 
     * @return The first card in the deck pile.
     */
    public Card takeFromDeck() {
        if (deck.isEmpty())
            shuffleDeck();
        return deck.remove(0);
    }

    /**
     * Shuffles the deck.
     */
    public void shuffleDeck() {
        discardPile.forEach(card -> card.shuffleIn(deck));
        Collections.shuffle(deck);
        discardPile.clear();
    }

    /**
     * A player draws from deck the choosen amount of cards.
     * 
     * @param player The player that will draw the cards.
     * @param times  The amount of cards to be drawn.
     */
    public void dealFromDeck(Player player, int times) {
        while (times-- > 0) {
            // Add card
            Card card = takeFromDeck();
            player.getHand().add(card);

            // Notify
            HashMap<String, Object> data = card.getData();
            data.putAll(player.getData());

            if (!(player instanceof GameAI))
                notifyToCU(Event.USER_DREW, data);
            else
                notifyToCU(Event.AI_DREW, data);
        }
    }

    /**
     * Jumps to the turn ahead by the given amount.
     * 
     * @param ahead The amount of turns to skip.
     */
    public void advanceTurn(int ahead) {
        int newTurn = (getTurn() + ahead) % players.length;
        setTurn(newTurn);
    }

    /**
     * Sends the message to the CUModel. It has the same effect of
     * <p>
     * 
     * <pre>
     * CUModel.communicate(event, data)
     * </pre>
     * </p>
     * but it stops the message to be sent if the game is dead (i.e., interrupted)
     * 
     * @param event
     * @param data
     */
    public void notifyToCU(Event event, HashMap<String, Object> data) {
        if (!isBlocked())
            CUModel.communicate(event, data);
    }
}