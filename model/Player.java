package model;

import java.util.Optional;

import controller.Controller;
import model.cards.Card;
import model.cards.Hand;

/**
 * This class contains all the info about the player state.
 */
public class Player {
    // !Visibilità default per usarle nel package
    String nickname;
    Hand hand;
    Optional<Controller> controller;

    public Player(String nickname, Card... cards) {
        this.nickname = nickname;
        hand = new Hand(cards);
        
        controller = Optional.empty();
    }

    /* GETTERS AND SETTERS */
    /* ------------------- */
    public String getNickname() {
        return nickname;
    }
    
    public Hand getHand() {
        return hand;
    }

    public void setController(Controller c) {
        controller = Optional.of(c);
    }

    /* OTHERS */
    /* ------ */
    protected void addCard(Card card) {
        hand.add(card);
        // !Potrebbe essere inefficiente e spiacevole sortare ad ogni pescata
        hand.arrange();
    }
}
