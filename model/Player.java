package model;

import java.util.TreeSet;

import model.cards.Card;
import model.cards.Hand;
import model.effects.Effect;

/**
 * This class contains all the info about the player state.
 */
public class Player {
    private String nickname;
    boolean isHuman;
    Hand hand;
    private int ID;
    private TreeSet<Effect> conditions;

    public Player(String nickname, boolean isHuman, Card... cards) {
        this.nickname = nickname;
        this.isHuman = isHuman;
        hand = new Hand(cards);

        conditions = new TreeSet<>();
    }

    /* GETTERS AND SETTERS */
    /* ------------------- */
    public String getNickname() {
        return nickname;
    }

    public boolean isHuman() {
        return isHuman;
    }

    public Hand getHand() {
        return hand;
    }

    public int getID() {
        return ID;
    }

    /* OTHERS */
    /* ------ */
    protected void addCard(Card card) {
        hand.add(card);
        hand.arrange(); // TODO Potrebbe essere inefficiente e spiacevole sortare ad ogni pescata
    }

    public void addCondition(Effect condition) {
        conditions.add(condition);
    }

    public void consumeConditions() {
        conditions.forEach(effect -> effect.cast(this, null));
        conditions.clear();
    }
}
