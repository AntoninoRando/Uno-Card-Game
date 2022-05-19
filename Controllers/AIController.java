package Controllers;
import CardsTools.Card;
import CardsTools.Hand;
import GameTools.Player;

public class AIController extends Controller {
    // CONSTRUCTORS
    public AIController(Player bringer) {
        this.bringer = bringer;
    }

    // METHODS
    @Override
    public void makePlay() {
        int index = getPlay();

        if (index == -1) {
            drawFromDeck();
            System.out.println(bringer.getNickname() + " drew 1 card.");
            return;
        }

        Card toPlay = bringer.getHand().remove(index);
        game.playCard(toPlay);
        System.out.println(bringer.getNickname() + " played " + toPlay);
    }

    // !La best play dovrebbe considerare tanttissime cose, le carte giocate e le
    // carte degli avversari. Ad esempio se l'avversario pesca su giallo, se hai un
    // cambio giro giallo è bene giocarlo per far (probabilmente) ripescare
    // l'avversario
    public int getPlay() {
        Hand hand = bringer.getHand();
        for (int i = 0; i < hand.getSize(); i++) {
            if (hand.getCard(i).isPlayable(game)) {
                return i;
            }
        }
        return -1;
    }
}
