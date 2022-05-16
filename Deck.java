import java.util.Arrays;

public class Deck {
    private int size;
    // !Non so se sia più conveniente fare una List o un array dove rimpiazziamo le carte pescate con null
    private Card[] cards;

    public Deck(Card[] cards) {
        this.cards = cards;
        size = cards.length;
    }

    public int getSize() {
        return size;
    }

    public void shuffleDeck() {
        Arrays.sort(cards);
    }

    public Card deal(int index) throws Exception {
        // We handle the IndexOutOfBoundsException
        if (index > size)
            index = size;
        else if (index < 0)
            index = 0;
        
        Card cardDrawn = cards[index];

        // !Dovrei definire una custom exception
        if (cardDrawn == null)
            throw new Exception("There is no card at this position");
            
        cards[index] = null;
        return cardDrawn;
    }
}
