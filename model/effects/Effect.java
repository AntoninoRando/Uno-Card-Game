package model.effects;
import model.cards.Card;

import java.util.LinkedList;

import model.Player;

public class Effect {
    Player sourcePlayer;
    Card sourceCard;
    Player target;
    LinkedList<Runnable> steps = new LinkedList<>();

    public void cast() {
        steps.forEach(Runnable::run);
    };

    public void merge(Effect after) {
        steps.addAll(after.steps);
    }
}