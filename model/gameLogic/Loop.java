package model.gameLogic;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

import events.EventManager;
import events.EventType;
import events.InputListener;

import prefabs.Card;
import prefabs.Player;

import model.data.PlayerData;




import controller.controls.Controller;

public class Loop implements InputListener {
    private static Loop instance;

    public static Loop getInstance() {
        if (instance == null)
            instance = new Loop();
        return instance;
    }

    private Loop() {
        choiceTypes = new HashMap<>();

        choiceTypes.put("card", () -> {
            Player player = g.getCurrentPlayer();
            Card c = (Card) choice;
            if (g.isPlayable(c)) {
                Actions.changeCurrentCard(c);
                player.getHand().remove(c);
                c.getEffect().ifPresent(effect -> effect.cast(player, c));
                events.notify(EventType.CARD_CHANGE, c);
                events.notify(EventType.PLAYER_PLAYED_CARD, player);
                events.notify(EventType.PLAYER_HAND_DECREASE, player);

                if (!player.info().isHuman()) 
                    return true;
                
                events.notify(EventType.USER_PLAYED_CARD, c);

                if (player.getHand().size() == 1 && !unoDeclared)
                    Actions.dealFromDeck(g.getCurrentPlayer(), 2);

                return true;
            } else {
                events.notify(EventType.INVALID_CARD, c);
                return false;
            }
        });
        choiceTypes.put("draw", () -> {
            Actions.dealFromDeck(g.getCurrentPlayer());
            return true;
        });
        choiceTypes.put("unoDeclared", () -> {
            startUnoTimer();
            return false;
        });
        choiceTypes.put("cardPosition", () -> {
            try {
                choice = g.getCurrentPlayer().getHand().get((int) choice);
                return choiceTypes.get("card").get();
            } catch (IndexOutOfBoundsException e) {
                events.notify(EventType.INVALID_CARD, choice);
                return false;
            }
        });
    }

    public static EventManager events = new EventManager();

    static HashMap<String, Supplier<Boolean>> choiceTypes;

    private static Game g;
    private static Player player;
    static Object choice;
    static String choiceType;

    private static Controller[] users;

    static boolean unoDeclared;

    LinkedList<Phase> phases = new LinkedList<>();
    static int currentPhase;

    private static long timeStart;

    public void setupGame(TreeMap<Integer, Player> players, Controller... users) {
        Game.reset();
        g = Game.getInstance();
        g.setPlayers(players);

        Loop.users = users;
        for (Controller c : users) {
            c.setInputListener(this);
            c.start();
        }

        g.restoreTurnOrder();
        phases.add(Phases.START_TURN);
        phases.add(Phases.MAKE_CHOICE);
        phases.add(Phases.PARSE_CHOICE);
        phases.add(Phases.RESOLVE_CHOICE);
        phases.add(Phases.END_TURN);
    }

    /* ------------------------------ */

    public void play() {
        setupFirstTurn();
        timeStart = System.currentTimeMillis();
        try {
            while (!g.isOver()) {
                boolean validChoice = phases.get(currentPhase).apply(this, Game.getInstance());

                if (g.didPlayerWin(g.getCurrentPlayer())) {
                    endGame(false);
                    return;
                }

                if (phases.get(currentPhase) == Phases.RESOLVE_CHOICE && !validChoice)
                    currentPhase = 1;
                else
                    currentPhase = (++currentPhase) % phases.size();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            // May be here because of the "reset()" method
        }
    }

    private void setupFirstTurn() {
        events.notify(EventType.GAME_READY, g.getPlayers().toArray(Player[]::new));

        Actions.shuffleDeck();
        Card firstCard = Actions.takeFromDeck();
        Actions.changeCurrentCard(firstCard);
        events.notify(EventType.CARD_CHANGE, firstCard);

        for (Player p : g.getPlayers())
            Actions.dealFromDeck(p, 7);

        player = g.getPlayerByTurn(0);

        for (Controller c : users)
            c.setupControls();

        events.notify(EventType.GAME_START, g.getPlayers().toArray(Player[]::new));
    }

    public void endGame(boolean interrupted) {
        Player winner = g.getCurrentPlayer();
        int xpEarned = (int) ((System.currentTimeMillis() - timeStart) / 60000F); // xpEarned = minutes elapsed from the
                                                                                  // start of the game.
        PlayerData.addXp(xpEarned);

        if (!interrupted) {
            boolean humanWon = g.getCurrentPlayer().info().isHuman();
            if (humanWon) {
                PlayerData.addXp(5);
                xpEarned += 5;
            }
            PlayerData.addGamePlayed(humanWon);
            events.notify(EventType.PLAYER_WON, winner);
        }

        Game.reset();

        // TODO convertire i metodi statici in non, così che basta fare instance = null
        // per resettare... per fare ciò però bisogna ripristinare tutti gli eventi ogni
        // volta
        instance = null;
        g = null;
        player = null;
        choice = null;
        choiceType = null;
        users = null;
        unoDeclared = false;
        currentPhase = 0;
        timeStart = 0;

        events.notify(EventType.RESET);
    }

    @Override
    public void accept(Object choice, Player source) {
        synchronized (this) {
            // We use == instead of equals because they have to be the same object
            if (source != player) {
                events.notify(EventType.INVALID_CARD, choice);
                return;
            }
            Loop.choice = choice;
            notify();
        }
    }
    
    // 

    private Consumer<Card> selectionEvent;
    
    public void setSeleciontEvent(Consumer<Card> selectionEvent) {
        this.selectionEvent = selectionEvent;
    }

    public void completeSelectionEvent(Card card) {
        selectionEvent.accept(card);
        selectionEvent = null;
    }

    //

    private void startUnoTimer() {
        Player unoer = player;
        events.notify(EventType.UNO_DECLARED);
        unoDeclared = true;

        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                }
                // We use == because they have to be the same. We check if it is still the unoer
                // turn because we don't want to modify the unoDeclared for others player.
                if (unoer == player)
                    unoDeclared = false;
            }
        }.start();
    }
}