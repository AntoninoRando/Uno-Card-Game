package model;

import model.cards.Card;
import model.cards.Hand;

/**
 * This class contains all the info about the player state.
 */
public class Player {
    private String nickname;
    boolean isHuman;
    Hand hand;
    private int ID;

    public Player(String nickname, boolean isHuman, Card... cards) {
        this.nickname = nickname;
        this.isHuman = isHuman;
        hand = new Hand(cards);
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
}
