package model.gameObjects;

public class SimpleCard extends Card {
    public SimpleCard(Suit suit, int value) {
        super(suit, value);
    }

    /**
     * It does nothing.
     */
    @Override
    public void play() {  
    }

}
