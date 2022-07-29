package model.effects;

import java.util.function.Consumer;
import java.util.function.Function;

import model.Actions;
import model.Game;
import model.Loop;
import model.Player;
import model.cards.Card;

public class EffectBuilder {
    private Effect effect;

    public EffectBuilder() {
        effect = new Effect();
    }

    public Effect build() {
        return effect;
    }

    public EffectBuilder directTarget(Player target) {
        effect.steps.add(() -> effect.targetPlayer = target);
        return this;
    }

    public EffectBuilder directTargetToFollowing(int ahead) {
        effect.steps.add(() -> {
            Game g = Game.getInstance();
            Player target = g.getPlayer();
            for (int i = ahead; i > 0; i--)
                target = g.getNextPlayer(target);
            effect.targetPlayer = target;
        });
        return this;
    }

    public EffectBuilder skipCurrentTurn() {
        effect.steps.add(() -> Actions.skipTurn());
        return this;
    }

    public EffectBuilder skipTargetTurn() {
        effect.steps.add(() -> effect.targetPlayer.addCondition(new EffectBuilder().skipCurrentTurn().build()));
        return this;
    }

    public EffectBuilder targetDraws(int quantity) {
        effect.steps.add(() -> Actions.dealFromDeck(effect.targetPlayer, quantity));
        return this;
    }

    public EffectBuilder reverseTurnOrder() {
        effect.steps.add(() -> {
            Game g = Game.getInstance();

            Function<Player, Player> oldNext = g.getNextPlayerEvaluator();
            g.setNextPlayerEvaluator(player -> {
                Player next = null;
                for (Player p : g.players())
                    if (oldNext.apply(p) == player) {
                        next = p;
                        break;
                    }
                return next;
            });
        });
        return this;
    }

    public EffectBuilder transformInto(Card card) {
        effect.steps.add(() -> {
            Actions.transformCard(effect.sourceCard, card);
        });
        return this;
    }

    public EffectBuilder transformIntoTarget() {
        effect.steps.add(() -> {
            Actions.transformCard(effect.sourceCard, effect.targetCard);
        });
        return this;
    }

    public EffectBuilder selectOneCardOf(Card... cards) {
        effect.steps.add(() -> {
            if (!effect.sourcePlayer.isHuman()) {
                Loop.getInstance().events.notify("cardSelection", effect.sourcePlayer);
                effect.targetCard = cards[(int) Math.random() * 4];
                return;
            } 

            Object[] data = new Object[cards.length + 1];
            data[0] = (Consumer<Card>) card -> effect.targetCard = card;
            for (int i = 0; i < cards.length; i++)
                data[i+1] = cards[i];
            Loop.getInstance().events.notify("cardSelection", data);
        });
        return this;
    }
}
