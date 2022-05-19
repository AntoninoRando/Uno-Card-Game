package Controllers;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import CardsTools.Card;
import GameTools.Player;

public class HumanController extends Controller {
    // VARIABLES
    // !penso ci voglia un try-catch-finally quando uso gli scanner. Inoltre non so
    // se vada bene perche' non capisco come funzioni. Penso si apra quando faccio
    // un print e l'esecuzione del codice si blocchi quando lo uso, perché quando lo
    // uso aspetta sicuro un comando.
    private Scanner userInput = new Scanner(System.in);
    // ! Se provo a chiuderlo poi non mi da errore se richiamo il metodo:
    // https://stackoverflow.com/questions/17622556/scanner-method-opened-and-closed-twice.
    // Inoltre da quello che vedo su questo comment, siccome lo scanner
    // rimane lo stesso (perchè è sullo stesso input stream), dovrei
    // renderlo un metodo della classe

    // CONSTRUCTORS
    public HumanController(Player bringer) {
        this.bringer = bringer;
    }

    /**
     * Play a generic card, but it will not be removed from the source (must be done
     * manually)
     * 
     * @param card
     * @param game
     * @return
     */
    private boolean playCard(Card card) {
        if (!game.playCard(card)) {
            // !Usare StringBuilder
            System.out.println("Can't play " + card.toString() + " now! Try different.");
            return false;
        }
        card.getEffect().signalToThis("play", game, game.getControllers().indexOf(this));
        return true;
    }

    // METHODS
    @Override
    public int getPlay() { //!Da fare
        return 2;
    }

    @Override
    public void makePlay() {
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

            String line = userInput.nextLine();

            if (line.equals("draw")) {
                drawFromDeck();
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
            for (String token : line.split(" ")) {
                int index = 0;
                try {
                    index = Integer.parseInt(token);
                } catch (NumberFormatException e) {
                    System.out.print("Invalid input, type something else: ");
                    break;
                }

                Card cardSelected = bringer.getHand().getCard(index - 1); // !L'input dato parte a contare da 1, quindi
                                                                          // index-1
                cardsPlayed.add(cardSelected);

                if (cardSelected.isPlayable(lastCard))
                    indicies.add(index - 1);
                lastCard = cardSelected;
            }
            for (Card card : cardsPlayed) {
                playCard(card);
            }
            // !Siccome usiamo un treeSet gli indici sono ordinati e non serve shiftare gli
            // indici
            for (int index : indicies) {
                bringer.getHand().remove(index); // !Gli idici sono già diminuiti di 1 rispetto all'input
            }

            game.checkWin(this);
        }
    }
}
