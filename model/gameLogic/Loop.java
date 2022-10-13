package model.gameLogic;

import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.Supplier;

import events.toModel.InputListener;
import events.toModel.InputType;
import events.toView.EventManager;
import events.toView.EventType;

import prefabs.Card;
import prefabs.Player;

import model.data.Info;
import model.data.PlayerData;

/**
 * A class that will modify the game state.
 */
public class Loop implements InputListener {
    private static Loop instance;

    public static Loop getInstance() {
        if (instance == null)
            instance = new Loop();
        return instance;
    }

    public static EventManager events = new EventManager();
    private Object choice;
    private String choiceType;
    private boolean unoDeclared;
    private LinkedList<Supplier<Boolean>> phases;
    private int currentPhase;
    private long timeStart;
    private boolean isPaused;

    /**
     * Sets the players of the games and resets the game state.
     * 
     * @param players The players.
     */
    public void setupGame(Player[] players) {
        phases = new LinkedList<>();
        phases.add(() -> startTurn());
        phases.add(() -> makeChoice());
        phases.add(() -> parseChoice());
        phases.add(() -> resolveChoice());
        phases.add(() -> endTurn());
        Game.reset();
        Game.getInstance().setPlayers(players);
        Game.getInstance().restoreTurnOrder();
    }

    /**
     * The main loop of the UNO game. Call this method to play the game on the
     * current game state.
     */
    public void play() {
        setupFirstTurn();
        timeStart = System.currentTimeMillis();
        try {
            while (!Game.getInstance().isOver() && !isPaused) {
                boolean validChoice = phases.get(currentPhase).get();

                if (Game.getInstance().didPlayerWin(Game.getInstance().getCurrentPlayer())) {
                    endGame(false);
                    return;
                }

                currentPhase = (currentPhase == 3 && !validChoice) ? 1 : (++currentPhase) % phases.size();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            // May be here because of the "reset()" method
        }
    }

    /**
     * Setup the first turn as explained in the UNO rules.
     */
    private void setupFirstTurn() {
        events.notify(EventType.GAME_READY, Game.getInstance().getPlayers());

        Actions.shuffleDeck();
        Card firstCard = Actions.takeFromDeck();
        Actions.changeCurrentCard(firstCard);
        events.notify(EventType.CARD_CHANGE, firstCard);

        for (Player p : Game.getInstance().getPlayers())
            Actions.dealFromDeck(p, Game.getInstance().getFirstHandSize());

        events.notify(EventType.GAME_START, Game.getInstance().getPlayers());
    }

    /**
     * Stops the game.
     * 
     * @param interrupted True if the game was interruped by the user, false if the
     *                    game ended because one player won.
     */
    public void endGame(boolean interrupted) {
        Player winner = Game.getInstance().getCurrentPlayer();
        int xpEarned = (int) ((System.currentTimeMillis() - timeStart) / 60000F); // xpEarned = minutes elapsed from the
                                                                                  // start of the game.
        PlayerData.addXp(xpEarned);

        if (!interrupted) {
            boolean humanWon = Game.getInstance().getCurrentPlayer().isHuman();
            if (humanWon) {
                PlayerData.addXp(5);
                xpEarned += 5;
            }
            PlayerData.addGamePlayed(humanWon);
            events.notify(EventType.PLAYER_WON, winner);
            Info.events.notify(EventType.XP_EARNED, xpEarned);
        }

        Game.reset();
        instance = null;

        events.notify(EventType.RESET);
    }

    // Getters and Setters

    /**
     * Sets the next phase. The current phase will however end.
     * 
     * @param phaseNumber The number of phase.
     */
    void jumpToPhase(int phaseNumber) {
        currentPhase = phaseNumber;
    }

    /**
     * Gets the amount of phases.
     */
    int getPhasesQuantity() {
        return phases.size();
    }

    /**
     * Gets the choice that will determine the user action for this turn.
     * 
     * @return The user choice.
     */
    Object getChoice() {
        return choice;
    }

    /**
     * Sets the enemy AI action of the turn.
     * 
     * @param choice The choice that describes the action that the AI will perform.
     */
    void setChoice(Object choice) {
        this.choice = choice;
    }

    String getChoiceType() {
        return choiceType;
    }

    void setChoiceType(String choiceType) {
        this.choiceType = choiceType;
    }

    // Loop logic

    private boolean startTurn() {
        events.notify(EventType.TURN_START, Game.getInstance().getCurrentPlayer());
        Game.getInstance().getCurrentPlayer().consumeConditions();
        return true;
    };

    private boolean makeChoice() {
        // User
        if (Game.getInstance().getCurrentPlayer().isHuman())
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
        // Enemy
        else
            Game.getInstance().getCurrentPlayer().getHand().stream().filter(Game.getInstance()::isPlayable).findAny()
                    .ifPresentOrElse(c -> setChoice(c), () -> setChoice("draw"));
        return true;
    };

    private boolean parseChoice() {
        if (getChoice() instanceof Card)
            setChoiceType("card");
        else if (getChoice() instanceof String)
            setChoiceType((String) choice);
        return true;
    };

    private boolean resolveChoice() {
        switch (getChoiceType()) {
            case "card":
                Player player = Game.getInstance().getCurrentPlayer();
                Card card = (Card) choice;
                if (Game.getInstance().isPlayable(card)) {
                    Actions.changeCurrentCard(card);
                    player.getHand().remove(card);
                    card.getEffect().ifPresent(effect -> effect.cast(player, card));
                    events.notify(EventType.CARD_CHANGE, card);
                    events.notify(EventType.PLAYER_PLAYED_CARD, player);
                    events.notify(EventType.PLAYER_HAND_DECREASE, player);

                    if (!player.isHuman())
                        return true;

                    events.notify(EventType.USER_PLAYED_CARD, card);

                    if (player.getHand().size() == 1 && !unoDeclared)
                        Actions.dealFromDeck(Game.getInstance().getCurrentPlayer(), 2);

                    return true;
                } else {
                    events.notify(EventType.INVALID_CARD, card);
                    return false;
                }
            case "draw":
                Actions.dealFromDeck(Game.getInstance().getCurrentPlayer());
                return true;
            case "unoDeclared":
                startUnoTimer();
                return false;
            default:
                return false;
        }
    };

    private boolean endTurn() {
        events.notify(EventType.TURN_END, Game.getInstance().getCurrentPlayer());
        Game.getInstance().setTurn(Game.getInstance().getNextPlayer());
        setChoice(null);
        setChoiceType(null);
        return true;
    };

    // Interface methods

    @Override
    public void acceptInput(InputType inputType, Object choice) {
        switch (inputType) {
            case TURN_DECISION:
                synchronized (this) {
                    // Not user turn
                    if (!Game.getInstance().getCurrentPlayer().isHuman()) {
                        events.notify(EventType.INVALID_CARD, choice);
                        return;
                    }
                    this.choice = choice;
                    notify();
                }
                break;
            case SELECTION:
                runDecontexPhase(choice);
                break;
            default:
                // TODO
                break;
        }
    }

    // Additional Loop logic

    /**
     * A phase that can be executed and changed any time. This phase takes an object
     * as input and performs an action with it.
     */
    private Consumer<Object> decontexPhase;

    /**
     * Sets an additional phase that can be executed and changed at any time.
     * 
     * @param decontexPhase The phase to run later, with the
     *                      <code>executeDecontexPhase</code> method.
     */
    public void setDecontexPhase(Consumer<Object> decontexPhase) {
        this.decontexPhase = decontexPhase;
    }

    /**
     * Executes the additional phase, setted with the <code>setDecontexPhase</code>
     * method. After the new phase ends, it will be deleted.
     * 
     * @param data The data that the phase may use to execute.
     */
    public void runDecontexPhase(Object data) {
        decontexPhase.accept(data);
        decontexPhase = null;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPause(boolean arrivalState) {
        isPaused = arrivalState;
    }

    //

    private void startUnoTimer() {
        Player unoer = Game.getInstance().getCurrentPlayer();
        events.notify(EventType.UNO_DECLARED);
        unoDeclared = true;

        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                }
                if (unoer == Game.getInstance().getCurrentPlayer())
                    unoDeclared = false;
            }
        }.start();
    }
}