package model.effects;

import model.cards.Card;

public abstract class Effect {
    protected int target = 1;
    protected int source = 1;
    protected Card card;

    public abstract void dispatch(Card card, int source);
}
