package model.gameLogic;

import java.util.LinkedList;

import prefabs.Card;
import prefabs.Player;

public class Effect implements Comparable<Effect> {
    Player sourcePlayer;
    Card sourceCard;
    Player targetPlayer;
    Card targetCard;
    LinkedList<Runnable> steps = new LinkedList<>();
    int priority;

    public void cast(Player soucePlayer, Card sourceCard) {
        this.sourcePlayer = soucePlayer;
        this.sourceCard = sourceCard;
        steps.forEach(Runnable::run);
    };

    @Override
    public int compareTo(Effect eff) {
        return Integer.compare(priority, eff.priority);
    }
}