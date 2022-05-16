public class Player {
    private String nickname;
    private Hand hand;

    public Player(String nickname, Card... cards) {
        this.nickname = nickname;
        this.hand = new Hand(cards);
    }

    public Card getCard(int index) {
        return hand.getCard(index);
    }

    public Card removeCard(int index) {
        return hand.remove(index);
    }
}
