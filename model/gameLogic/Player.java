package model.gameLogic;

import java.util.TreeSet;

/**
 * This class contains all the info about the player state.
 */
public class Player {
    private String nickname;
    boolean isHuman;
    CardGroup hand;
    private int ID;
    private TreeSet<Effect> conditions;

    public Player(String nickname, boolean isHuman, Card... cards) {
        this.nickname = nickname;
        this.isHuman = isHuman;
        hand = new CardGroup(cards);

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

    public CardGroup getHand() {
        return hand;
    }

    public int getID() {
        return ID;
    }

    /* OTHERS */
    /* ------ */
    public void addCondition(Effect condition) {
        conditions.add(condition);
    }

    public void consumeConditions() {
        conditions.forEach(effect -> effect.cast(this, null));
        conditions.clear();
    }
}
