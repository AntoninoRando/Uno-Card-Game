import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

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
            System.out.println("Can't play " + card.toString() + " now! Try different.");
            return false;
        }

        game.setTerrainCard(card);
        bringer.removeCard(index);
        return true;
    }

    /**
     * Play a generic card, but it will not be removed from the source (must be done
     * manually)
     * 
     * @param card
     * @param game
     * @return
     */
    private boolean playCard(Card card, GameManager game) {
        if (!card.isPlayable(game)) {
            // !Usare StringBuilder
            System.out.println("Can't play " + card.toString() + " now! Try different.");
            return false;
        }

        game.setTerrainCard(card);
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

        // If no card was played
        Set<Integer> indicies = new TreeSet<Integer>(Collections.reverseOrder()); // !Usiamo l'ordine inverso così che
                                                                                  // non dobbiamo shiftare gli indici
                                                                                  // man mano che li usiamo per togliere
                                                                                  // carte.
        List<Card> cardsPlayed = new LinkedList<Card>();
        while (indicies.isEmpty()) { // Repeat until player play a valid card or draw. We use indicies to check
                                     // instead of cardPlayed because cardsPlayed store also non valid selected
                                     // cards. If player draw the loop will break
            // We clear the collections because in case the input must be repeated. Must be
            // done at the start of code block, otherwise the loop will never end
            cardsPlayed.clear();
            indicies.clear();

            String line = selection.nextLine();

            if (line.equals("draw")) {
                drawCard(0, game);
                break;
            }

            // !Non so perche' ma while (selection.hasNextInt()) { \* ... *\ } funziona per
            // iterare su tutti gli interi (anche senza l'uso di selection.nextLine()) ma
            // continua a prendere input anche dopo l'invio.
            // while (selection.hasNextInt()) {
            // int index = selection.nextInt();r
            // playCard(index, game);
            // }
            Card lastCard = game.getTerrainCard();
            for (String position : line.split(" ")) {
                int index = Integer.parseInt(position);

                Card cardSelected = bringer.getHand().getCard(index - 1); // !L'input dato parte a contare da 1, quindi
                                                                          // index-1
                cardsPlayed.add(cardSelected);

                if (cardSelected.isPlayable(lastCard))
                    indicies.add(index - 1);
                lastCard = cardSelected;
            }
            for (Card card : cardsPlayed) {
                playCard(card, game);
            }
            // !Siccome usiamo un treeSet gli indici sono ordinati e non serve shiftare gli
            // indici
            for (int index : indicies) {
                bringer.getHand().remove(index); // !Gli idici sono già diminuiti di 1 rispetto all'input
            }
        }
    }
}
