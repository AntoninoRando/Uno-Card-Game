package model.listeners;

import model.cards.Card;

@FunctionalInterface
public interface TerrainListener {
    public void cardChanged(Card c);
}
