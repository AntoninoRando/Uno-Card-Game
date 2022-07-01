package model.listeners;

import model.cards.Hand;

@FunctionalInterface
public interface HandListener {
    public void handChanged(Hand hand, String nickname);
}
