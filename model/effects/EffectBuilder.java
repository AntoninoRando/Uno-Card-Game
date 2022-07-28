package model.effects;

import java.util.function.Function;

import model.Actions;
import model.Game;
import model.Player;

public class EffectBuilder {
    private Effect effect;

    public EffectBuilder() {
        effect = new Effect();
    }

    public Effect build() {
        return effect;
    }

    public EffectBuilder directTarget(Player target) {
        effect.steps.add(() -> effect.target = target);
        return this;
    }

    public EffectBuilder directTargetToFollowing(int ahead) {
        effect.steps.add(() -> {
            Game g = Game.getInstance();
            Player target = g.getPlayer();
            for (int i = ahead; i > 0; i--)
                target = g.getNextPlayer(target);
            effect.target = target;
        });
        return this;
    }

    public EffectBuilder skipCurrentTurn() {
        effect.steps.add(() -> Actions.skipTurn());
        return this;
    }

    public EffectBuilder skipTargetTurn() {
        effect.steps.add(() -> effect.target.addCondition(new EffectBuilder().skipCurrentTurn().build()));
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

    public EffectBuilder draw(int quantity) {
        effect.steps.add(() -> effect.target.addCondition(new EffectBuilder().skipCurrentTurn().build()));
        return this;
    }
}
