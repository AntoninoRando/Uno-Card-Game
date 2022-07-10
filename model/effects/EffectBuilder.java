package model.effects;

// TODO all

// import model.events.EventListener;

// import java.util.function.Predicate;

// import model.Actions;
// import model.Game;
// import model.Player;
// import model.cards.Card;

public class EffectBuilder {
    // private Player source;
    // private Player target;
    // private Card card;
    
    // private Effect[] steps;
    // private int count;

    // public EffectBuilder(int capacity) {
    //     steps = new Effect[capacity];
    // }

    // public Effect build() {
    //     Effect eff = Effect.ofNothing();
    //     for (Effect step : steps) 
    //         eff = eff.andThen(step);
    //     return eff;
    // }

    // public EffectBuilder addForceGround() {
    //     steps[count] = () -> Actions.changeCurrentCard(card);
    //     count++;
    //     return this;
    // }

    // public EffectBuilder addTryGround() {
    //     steps[count] = () -> Actions.tryChangeCard(card);
    //     count++;
    //     return this;
    // }

    // TODO sistemare
    // public EffectBuilder addDraw(int times) {
    //     steps[i] = () -> Actions.dealFromDeck(eff.target, times);
    //     i++;
    //     return this;
    // }

    // public EffectBuilder addBlockTurn() {
    //     steps[count] = () -> {
    //         EventListener blocker = new EventListener() {
    //             @Override
    //             public void update(String __, Object data) {
    //                 if ((int) data != eff.target)
    //                     return;
    //                 Actions.skipTurn();
    //                 MainLoop.getInstance().events.unsubscribe("PlayerTurn", this);
    //             }
    //         };
    //         MainLoop.getInstance().events.subscribe("PlayerTurn", blocker);
    //     };
    //     i++;
    //     return this;
    // }

    // public EffectBuilder addChangePlayCondition(Predicate<Card> newCondition) {
    //     steps[count] = () -> Game.getInstance().setPlayConditon(newCondition);
    //     count++;
    //     return this;
    // }

    // public EffectBuilder addEvent(String eventType, EventListener event) {
    //     steps[i] = () -> {
    //         MainLoop ml = MainLoop.getInstance();
    //         ml.events.subscribe(eventType, event);
    //     };
    //     i++;
    //     return this;
    // }

    // public EffectBuilder triggerEvent(String eventType, Object data) {
    //     steps[i] = () -> MainLoop.getInstance().events.notify(eventType, data);
    //     i++;
    //     return this;
    // }

    // public EffectBuilder directTarget(int p) {
    //     steps[i] = () -> eff.changeTarget(p);
    //     i++;
    //     return this;
    // }

    // public EffectBuilder directTargetToFollowing(int ahead) {
    //     steps[i] = () -> eff.changeTarget(eff.target + ahead);
    //     i++;
    //     return this;
    // }

    // public EffectBuilder askForInput(String message) {
    //     steps[i] = () -> MainLoop.getInstance().events.notify("Warn", message);
    //     i++;
    //     return this;
    // }

    // public EffectBuilder addKeepTurn() {
    //     steps[i] = () -> Game.getInstance().setTurn(eff.source-1);
    //     i++;
    //     return this;
    // }
}