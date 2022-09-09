package model.gameLogic;

import java.util.stream.Stream;

import events.EventType;

import model.data.CardsInfo;

import prefabs.Card;
import prefabs.Player;

public class EffectBuilder {
    private Effect effect;

    public EffectBuilder() {
        effect = new Effect();
    }

    public Effect build() {
        return effect;
    }

    public Effect build(String... s) {
        int i = 0;
        int j = s.length;
        while (i < j) {
            switch (s[i]) {
                case "target":
                    i++;
                    directTargetToFollowing(Integer.parseInt(s[i]));
                    break;
                case "they pass":
                    skipTargetTurn();
                    break;
                case "they draw":
                    i++;
                    targetDraws(Integer.parseInt(s[i]));
                    break;
                case "reverse turn":
                    reverseTurnOrder();
                    break;
                case "become target":
                    transformIntoTarget();
                    break;
                case "select one of":
                    i++;
                    selectOneCardOf(s[i]);
                    break;
            }
            i++;
        }
        return effect;
    }

    public EffectBuilder directTarget(Player target) {
        effect.addStep((() -> effect.setTargetPlayer(target)));
        return this;
    }

    public EffectBuilder directTargetToFollowing(int ahead) {
        effect.addStep(() -> {
            Game g = Game.getInstance();
            effect.setTargetPlayer(g.getPlayerByTurn(g.getTurnOf(effect.getSourcePlayer()) + ahead));
        });
        return this;
    }

    public EffectBuilder skipCurrentTurn() {
        effect.addStep(() -> Actions.skipTurn());
        return this;
    }

    public EffectBuilder skipTargetTurn() {
        effect.addStep(() -> effect.getTargetPlayer().addCondition(new EffectBuilder().skipCurrentTurn().build()));
        return this;
    }

    public EffectBuilder targetDraws(int quantity) {
        effect.addStep(() -> Actions.dealFromDeck(effect.getTargetPlayer(), quantity));
        return this;
    }

    public EffectBuilder reverseTurnOrder() {
        effect.addStep(() -> {
            Game g = Game.getInstance();
            Player[] oldA = g.getTurnOrder();
            Player[] newA = new Player[oldA.length];
            int n = g.countPlayers();
            int currentTurn = g.getTurn();
            for (int i = 0; i < n; i++)
                newA[(i + currentTurn) % n] = oldA[((currentTurn - i) + n) % n];
            g.setTurnOrder(newA);
        });
        return this;
    }

    public EffectBuilder transformInto(Card card) {
        effect.addStep(() -> {
            Actions.transformCard(effect.getSourceCard(), card);
        });
        return this;
    }

    public EffectBuilder transformIntoTarget() {
        effect.addStep(() -> Actions.transformCard(effect.getSourceCard(), effect.getTargetCard()));
        return this;
    }

    public EffectBuilder selectOneCardOf(String cardsReprs) {
        effect.addStep(() -> {
            Card[] cards = Stream.of(cardsReprs.split("  ")).map(cn -> CardsInfo.allCards.get(cn).getCopy())
            .toArray(Card[]::new);

            if (!effect.getSourcePlayer().isHuman()) {
                effect.setTargetCard(cards[(int) (Math.random() * cards.length)]);
                return;
            }
            
            Loop.getInstance().setSeleciontEvent(card -> effect.setTargetCard(card));
            Loop.events.notify(EventType.USER_SELECTING_CARD, cards);
        });
        return this;
    }
}
