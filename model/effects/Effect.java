package model.effects;

import model.cards.Card;

import java.util.LinkedList;

import model.Player;

public class Effect implements Comparable<Effect> {
    Player sourcePlayer;
    Card sourceCard;
    Player targetPlayer;
    Card targetCard;
    LinkedList<Runnable> steps = new LinkedList<>();

    public void cast(Player soucePlayer, Card sourceCard) {
        this.sourcePlayer = soucePlayer;
        this.sourceCard = sourceCard;
        steps.forEach(Runnable::run);
    };

    public void merge(Effect after) {
        steps.addAll(after.steps);
    }

    int priority;

    @Override
    public int compareTo(Effect eff) {
        return Integer.compare(priority, eff.priority);
    }
}