import java.util.Scanner;

public class HumanController extends Controller {
    // CONSTRUCTORS
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

    private void drawCard(int index, GameManager game) {
        bringer.addCard(game.getDrawingDeck().remove(index));
    }

    @Override
    public void playCardsFromInput(GameManager game) {
        // !penso ci voglia un try-catch-finally quando uso gli scanner.
        Scanner selection = new Scanner(System.in); // ! Se provo a chiuderlo poi non mi da errore se richiamo il
                                                    // metodo:
                                                    // https://stackoverflow.com/questions/17622556/scanner-method-opened-and-closed-twice.
                                                    // Inoltre da quello che vedo su questo comment, siccome lo scanner
                                                    // rimane lo stesso (perchè è sullo stesso input stream), dovrei
                                                    // renderlo un metodo della classe
        System.out.print(
                bringer.getNickname() + ", select cards to play over " + game.getTerrainCard().toString()
                        + " or \"draw\" - " + bringer.getHand().toString() + ": ");

        String line = selection.nextLine();

        if (line.equals("draw")) {
            drawCard(0, game);
            selection.close();
            return;
        }

        // !Non so perche' ma while (selection.hasNextInt()) { \* ... *\ } funziona per
        // iterare su tutti gli interi (anche senza l'uso di selection.nextLine()) ma
        // continua a prendere input anche dopo l'invio.
        // while (selection.hasNextInt()) {
        // int index = selection.nextInt();
        // playCard(index, game);
        // }
        int shift = 0;
        for (String position : line.split(" ")) {
            int index = Integer.parseInt(position) - shift;
            if (playCard(index, game)) // If card was played shift the index digited by 1
                shift++;
        }
    }
}
