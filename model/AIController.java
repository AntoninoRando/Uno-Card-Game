package model;

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
            String played = bringer.getCard(i).toString();
            playCard(i);
            System.out.println(bringer.getNickname() + " played " + played);
            game.winCondition(this);
        }
    }

    // !La best play dovrebbe considerare tanttissime cose, le carte giocate e le
    // carte degli avversari. Ad esempio se l'avversario pesca su giallo, se hai un
    // cambio giro giallo è bene giocarlo per far (probabilmente) ripescare
    // l'avversario
    public int[] getPlay() {
        for (int i = 0; i < bringer.hand.size(); i++) {
            if (game.isPlayable(bringer.hand.get(i))) 
                return new int[] { i };
        }
        return new int[] { -1 };
    }
}
