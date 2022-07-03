package model.effects;

import model.events.EventListener;

import model.Actions;
import model.MainLoop;
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

    public EffectBuilder addBlockTurn() {
        steps[i] = () -> {
            EventListener blocker = new EventListener() {
                @Override
                public void update(Object data) {
                    if ((int) data != eff.target)
                        return;
                    Actions.skipTurn();
                    MainLoop.getInstance().events.unsubscribe("PlayerTurn", this);
                }
            };
            MainLoop.getInstance().events.subscribe("PlayerTurn", blocker);;
        };
        i++;
        return this;
    }

    public EffectBuilder directTarget(int p) {
        steps[i] = () -> eff.changeTarget(p);;
        i++;
        return this;
    }

    public EffectBuilder directTargetToFollowing(int ahead) {
        steps[i] = () -> eff.changeTarget(eff.target + ahead);
        i++;
        return this;
    }
}