package model.gameLogic;

import java.util.function.Consumer;

import events.EventType;
import model.data.CardsInfo;

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
                    String[] cardsNames = s[i].split("  ");
                    selectOneCardOf(cardsNames);
                    break;
            }
            i++;
        }
        return effect;
    }

    public EffectBuilder directTarget(Player target) {
        effect.steps.add(() -> effect.targetPlayer = target);
        return this;
    }

    public EffectBuilder directTargetToFollowing(int ahead) {
        effect.steps.add(() -> {
            Game g = Game.getInstance();
            effect.targetPlayer = g.getPlayerByTurn(g.getTurnOf(effect.sourcePlayer) + ahead);
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
        effect.steps.add(() -> {
            Actions.transformCard(effect.sourceCard, card);
        });
        return this;
    }

    public EffectBuilder transformIntoTarget() {
        effect.steps.add(() -> Actions.transformCard(effect.sourceCard, effect.targetCard));
        return this;
    }

    public EffectBuilder selectOneCardOf(Card... cards) {
        effect.steps.add(() -> {
            if (!effect.sourcePlayer.info().isHuman()) {
                effect.targetCard = cards[(int) (Math.random() * cards.length)];
                return;
            }

            Object[] data = new Object[cards.length + 1];
            data[0] = (Consumer<Card>) card -> effect.targetCard = card;
            for (int i = 0; i < cards.length; i++)
                data[i + 1] = cards[i];
            Loop.events.notify(EventType.USER_SELECTING_CARD, cards);
        });
        return this;
    }

    public EffectBuilder selectOneCardOf(String... cardsNames) {
        effect.steps.add(() -> {
            Card[] cards = new Card[cardsNames.length];
            for (int i = 0; i < cards.length; i++) {
                String fullName = cardsNames[i];
                cards[i] = CardsInfo.allCards.get(fullName).getCopy();
            }

            if (!effect.sourcePlayer.info().isHuman()) {
                effect.targetCard = cards[(int) (Math.random() * cards.length)];
                return;
            }

            Object[] data = new Object[cards.length + 1];
            data[0] = (Consumer<Card>) card -> effect.targetCard = card;
            for (int i = 0; i < cards.length; i++)
                data[i + 1] = cards[i];
            Loop.events.notify(EventType.USER_SELECTING_CARD, cards);
        });
        return this;
    }
}
