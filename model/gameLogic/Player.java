package model.gameLogic;

import java.util.TreeSet;

/**
 * This class contains all the info about the player state.
 */
public class Player {
    private String nickname;
    boolean isHuman;
    CardGroup hand;
    private TreeSet<Effect> conditions;
    private String iconPath = "resources/icons/night.png";

    public Player(String nickname, boolean isHuman) {
        this.nickname = nickname;
        this.isHuman = isHuman;
        hand = new CardGroup();
        conditions = new TreeSet<>();
    }

    public Player(String nickname, boolean isHuman, String iconPath) {
        this.nickname = nickname;
        this.isHuman = isHuman;
        this.iconPath = iconPath;
        hand = new CardGroup();
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

    public String getIconPath() {
        return iconPath;
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
