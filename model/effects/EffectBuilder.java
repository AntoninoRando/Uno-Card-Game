package model.effects;

import java.util.function.Consumer;

import model.Actions;
import model.cards.Card;

public class EffectBuilder {
    private int i;
    private Object[] arguments;
    private Consumer<Object>[] steps;

    private Effect eff;

    @SuppressWarnings("unchecked")
    public EffectBuilder(int capacity) {
        arguments = new Object[capacity];
        steps = new Consumer[capacity]; // Unchecked warning
    }

    public Effect build() {
        eff = new Effect() {
            @Override
            public void dispatch(Card card, int source) {
                this.card = card;
                this.source = source;
                this.target = source;
                for (int i = 0; i < steps.length; i++) 
                    steps[i].accept(arguments[i]);
                this.card = null;
                this.source = 1;
                this.target = 1;
            }    
        };
        return eff;
    }

    public EffectBuilder addForceGround() {
        Consumer<Object> step = (__) -> Actions.changeCurrentCard(eff.card);
        steps[i] = step; 
        arguments[i] = null;
        i++;
        return this;
    }

    public EffectBuilder addTryGround() {
        Consumer<Object> step = (__) -> Actions.tryChangeCard(eff.card);
        steps[i] = step; 
        arguments[i] = null;
        i++;
        return this;
    }

    public EffectBuilder directTarget(int p) {
        Consumer<Object> step = (targ) -> eff.target = ((int) targ);
        steps[i] = step; 
        arguments[i] = p;
        i++;
        return this;
    }

    public EffectBuilder addDraw(int times) {
        Consumer<Object> step = (x) -> Actions.dealFromDeck(eff.target, (int) x);
        steps[i] = step; 
        arguments[i] = times;
        i++;
        return this;
    }
}