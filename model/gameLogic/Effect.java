package model.gameLogic;

import java.util.LinkedList;

import model.gameEntities.Player;

/* --- Mine ------------------------------- */

import model.gameObjects.*;

/**
 * An effect that can be cast in relation to a card. On efect cast, the game
 * state could change.
 */
public class Effect implements Comparable<Effect> {
    /* --- Fields ----------------------------- */

    private Player sourcePlayer;
    private Card sourceCard;
    private Player targetPlayer;
    private Card targetCard;
    private LinkedList<Runnable> steps = new LinkedList<>();
    private int priority;

    /**
     * Gets the player that fired this effect.
     */
    Player getSourcePlayer() {
        return sourcePlayer;
    }

    /**
     * Sets the origin source player of this effect.
     * 
     * @param player The player who will fire this effect.
     */
    void setSourcePlayer(Player player) {
        sourcePlayer = player;
    }

    /**
     * @return The player that will be affected through this effect. Call this
     *         method during an effect step to retrieve the player that this effect
     *         is affecting from that step on.
     */
    Player getTargetPlayer() {
        return targetPlayer;
    }

    /**
     * Sets the player to affect through this effect. Call this method during an
     * effect step to set the player that will be targeted from that step on.
     * 
     * @param player The player to target.
     */
    void setTargetPlayer(Player player) {
        targetPlayer = player;
    }

    /**
     * Gets the card that holds this effect.
     */
    Card getSourceCard() {
        return sourceCard;
    }

    /**
     * Sets the origin source card of this effect.
     * 
     * @param card The card who will fire this effect.
     */
    void setSourceCard(Card card) {
        sourceCard = card;
    }

    /**
     * @return The card that will be affected through this effect. Call this method
     *         during an effect step to retrieve the card that this effect is
     *         affecting from that step on.
     */
    Card getTargetCard() {
        return targetCard;
    }

    /**
     * Sets the card to affect through this effect. Call this method during an
     * effect step to set the card that will be targeted from that step on.
     * 
     * @param player The card to target.
     */
    void setTargetCard(Card card) {
        targetCard = card;
    }

    /* --- Body ------------------------------- */

    /**
     * Adds a sub action to perform after all the previous.
     * 
     * @param step A step of the entire action that this effect will perform.
     */
    void addStep(Runnable step) {
        steps.add(step);
    }

    /**
     * Performs this effect and attributes its origin.
     * 
     * @param soucePlayer The player that fired this effect.
     * @param sourceCard  The card that fired this effect.
     */
    public void cast(Player soucePlayer, Card sourceCard) {
        this.sourcePlayer = soucePlayer;
        this.sourceCard = sourceCard;
        steps.forEach(Runnable::run);
    };

    @Override
    public int compareTo(Effect eff) {
        return priority - eff.priority;
    }
}