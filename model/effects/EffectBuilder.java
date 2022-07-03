package model.effects;

import model.Actions;
import model.cards.Card;

public class EffectBuilder {
    private int i;
    private Runnable[] steps;
    private Effect eff;

    public EffectBuilder(int capacity) {
        steps = new Runnable[capacity];
    }

    public Effect build() {
        eff = new Effect() {
            @Override
            public void dispatch(Card card, int source) {
                this.card = card;
                this.source = source;
                this.target = source;
                for (Runnable step : steps) 
                    step.run();
                this.card = null;
                this.source = 1;
                this.target = 1;
            }    
        };
        return eff;
    }

    public EffectBuilder addForceGround() {
        steps[i] = () -> Actions.changeCurrentCard(eff.card);
        i++;
        return this;
    }

    public EffectBuilder addTryGround() {
        steps[i] = () -> Actions.tryChangeCard(eff.card);
        i++;
        return this;
    }

    public EffectBuilder addDraw(int times) {
        steps[i] = () -> Actions.dealFromDeck(eff.target, times);
        i++;
        return this;
    }

    public EffectBuilder directTarget(int p) {
        steps[i] = () -> eff.target = (p);
        i++;
        return this;
    }

    public EffectBuilder directTargetToFollowing(int ahead) {
        steps[i] = () -> eff.target += ahead;
        i++;
        return this;
    }
}