package model.gameLogic;

import java.util.stream.Stream;

import events.toView.EventType;
import model.data.CardsInfo;

import prefabs.Card;
import prefabs.Player;

/**
 * A class able to build an effect specifying the order of actions to perform.
 * The possible actions are contained in this class.
 */
public class EffectBuilder {
    private Effect effect;

    public EffectBuilder() {
        effect = new Effect();
    }

    /**
     * 
     * @return The effect which action is specified by this EffectBuilder.
     */
    public Effect build() {
        return effect;
    }

    /**
     * Builds an Effect based on the string representation of its action.
     * 
     * @param s The string representation of the effect.
     * @return The effect that will perform the action specified.
     */
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

    /**
     * Changes the effect target.
     * 
     * @param target New effect target.
     * @return this.
     */
    public EffectBuilder directTarget(Player target) {
        effect.addStep((() -> effect.setTargetPlayer(target)));
        return this;
    }

    /**
     * Changes the effect target to the player following the source player.
     * 
     * @param ahead Amount of players ahead to reach the target player.
     * @return this.
     */
    public EffectBuilder directTargetToFollowing(int ahead) {
        effect.addStep(() -> {
            Game g = Game.getInstance();
            effect.setTargetPlayer(g.getPlayerByTurn(g.getTurnOf(effect.getSourcePlayer()) + ahead));
        });
        return this;
    }

    /**
     * Skips the current turn.
     * 
     * @return this.
     */
    public EffectBuilder skipCurrentTurn() {
        effect.addStep(() -> Actions.skipTurn());
        return this;
    }

    /**
     * Skips the turn when it's time for the target player to play.
     * 
     * @return this.
     */
    public EffectBuilder skipTargetTurn() {
        effect.addStep(() -> effect.getTargetPlayer().addCondition(new EffectBuilder().skipCurrentTurn().build()));
        return this;
    }

    /**
     * Gives cards from deck to the target player.
     * 
     * @param quantity The amount of cards.
     * @return this.
     */
    public EffectBuilder targetDraws(int quantity) {
        effect.addStep(() -> Actions.dealFromDeck(effect.getTargetPlayer(), quantity));
        return this;
    }

    /**
     * Reverse the current turn order.
     * 
     * @return this.
     */
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

    /**
     * Transforms the source card into another card.
     * 
     * @param card The new source card.
     * @return this.
     */
    public EffectBuilder transformInto(Card card) {
        effect.addStep(() -> {
            Actions.transformCard(effect.getSourceCard(), card);
        });
        return this;
    }

    /**
     * Transforms the source card into the target card.
     * 
     * @return this.
     */
    public EffectBuilder transformIntoTarget() {
        effect.addStep(() -> Actions.transformCard(effect.getSourceCard(), effect.getTargetCard()));
        return this;
    }

    /**
     * Opens a choices of cards to select. The selected card will become the target
     * card.
     * 
     * @param cardsReprs Cards names separated by 2 spaces: " ".
     * @return this.
     */
    public EffectBuilder selectOneCardOf(String cardsReprs) {
        effect.addStep(() -> {
            Card[] cards = Stream.of(cardsReprs.split("  ")).map(cn -> CardsInfo.allCards.get(cn).getCopy())
                    .toArray(Card[]::new);

            if (!effect.getSourcePlayer().isHuman()) {
                effect.setTargetCard(cards[(int) (Math.random() * cards.length)]);
                return;
            }

            Loop.getInstance().setDecontexPhase(card -> effect.setTargetCard((Card) card));
            Loop.events.notify(EventType.USER_SELECTING_CARD, cards);
        });
        return this;
    }
}
