import java.util.Scanner;

public class HumanController extends Controller {
    public HumanController(Player bringer) {
        this.bringer = bringer;
    }

    /**
     * 
     * @param index
     * @return true if the card has been played, false otherwise.
     */
    private boolean playCard(int index, GameManager game) {
        Card card = bringer.getCard(index);

        if (!card.isPlayable(game)) {
            // !Usare StringBuilder
            System.out.println("Can't play " + card.toString() + " now!");
            return false;
        }

        game.setTerrainCard(card);
        bringer.removeCard(index);
        return true;
    }

    // private boolean drawCard(int index, GameManager game) {

    // }

    public void playCardsFromInput(GameManager game) {
        // !Attenzione, perche' in questo modo gli indici variano se si giocano
        // piu'carte insieme. Ad esempio se si prende un elemento + l'ultimo, dara'
        // errore perche' l'ultimo si e' spostato appena preso il primo. Inoltre
        // penso ci voglia un try-catch-finally.
        Scanner selection = new Scanner(System.in);
        System.out.print("Select cards to play or \"draw\": ");

        while (selection.hasNextInt()) {
            int index = selection.nextInt();
            playCard(index, game);
        }

        selection.close();
    }
}
