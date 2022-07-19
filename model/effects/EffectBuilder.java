package model.effects;

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
            effect.target = g.getPlayer((g.getTurn(effect.sourcePlayer) + ahead) % g.countPlayers());
        });
        return this;
    }
}
