package Controllers;

import CardsTools.Card;
import CardsTools.Hand;

public class AIController extends Controller {
    // CONSTRUCTORS
    public AIController(Player bringer) {
        this.bringer = bringer;
    }

    // METHODS
    @Override
    public void makePlay() {
        int[] indices = getPlay();

        for (int i : indices) {
            if (i == -1) {
                drawFromDeck();
                System.out.println(bringer.getNickname() + " drew 1 card.");
                break;
            }

            // !Questa parte sotto funziona solo perché sarò un solo elemento, altrimenti
            // gli indici si spostano
            Card toPlay = bringer.getHand().remove(i);
            game.playCard(toPlay);
            System.out.println(bringer.getNickname() + " played " + toPlay);
        }
    }

    // !La best play dovrebbe considerare tanttissime cose, le carte giocate e le
    // carte degli avversari. Ad esempio se l'avversario pesca su giallo, se hai un
    // cambio giro giallo è bene giocarlo per far (probabilmente) ripescare
    // l'avversario
    public int[] getPlay() {
        Hand hand = bringer.getHand();
        for (int i = 0; i < hand.getSize(); i++) {
            if (hand.getCard(i).isPlayable(game)) {
                return new int[] { i };
            }
        }
        return new int[] { -1 };
    }
}
