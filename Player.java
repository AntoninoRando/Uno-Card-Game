public class Player {
    private String nickname;
    private Hand hand;

    public Player(String nickname, Card... cards) {
        this.nickname = nickname;
        this.hand = new Hand(cards);
    }

    public String getNickname() {
        return nickname;
    }

    public Hand getHand() {
        return hand;
    }

    public Card getCard(int index) {
        return hand.getCard(index);
    }

    public Card removeCard(int index) {
        return hand.remove(index);
    }

    public void addCard(Card card) {
        hand.add(card);
    }
}
