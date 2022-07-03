package model.effects;

import model.cards.Card;

public abstract class Effect {
    protected int target;
    protected int source;
    protected Card card;

    public abstract void dispatch(Card card, int source);

    protected void changeTarget(int newTarget) {
        target = newTarget;
    }
}