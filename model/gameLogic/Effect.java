package model.gameLogic;

import java.util.LinkedList;

import prefabs.Card;
import prefabs.Player;

/**
 * An effect type that can be cast in relation to a card. On efect cast, the game state could change.
 */
public class Effect implements Comparable<Effect> {
    private Player sourcePlayer;
    private Card sourceCard;
    private Player targetPlayer;
    private Card targetCard;
    private LinkedList<Runnable> steps = new LinkedList<>();
    private int priority;

    Player getSourcePlayer() {
        return sourcePlayer;
    }

    void setSourcePlayer(Player player) {
        sourcePlayer = player;
    }

    Player getTargetPlayer() {
        return targetPlayer;
    }

    void setTargetPlayer(Player player) {
        targetPlayer = player;
    }

    Card getSourceCard() {
        return sourceCard;
    }

    void setSourceCard(Card card) {
        sourceCard = card;
    }

    Card getTargetCard() {
        return targetCard;
    }

    void setTargetCard(Card card) {
        targetCard = card;
    }

    void addStep(Runnable step) {
        steps.add(step);
    }

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