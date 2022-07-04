package model;

import model.cards.Card;
import model.cards.Hand;

/**
 * This class contains all the info about the player state.
 */
public class Player {
    // !Visibilità default per usarle nel package
    String nickname;
    boolean isHuman;
    Hand hand;
    int ID;

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
    
    public Hand getHand() {
        return hand;
    }

    /* OTHERS */
    /* ------ */
    protected void addCard(Card card) {
        hand.add(card);
        // !Potrebbe essere inefficiente e spiacevole sortare ad ogni pescata
        hand.arrange();
    }
}
