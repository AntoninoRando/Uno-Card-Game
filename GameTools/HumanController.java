package GameTools;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import CardsTools.Card;

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
        if (!game.isPlayable(card)) {
            System.out.println("Can't play " + card.toString() + " now! Try different.");
            return false;
        }

        game.changeCurrentCard(card);
        return true;
    }

    // METHODS
    @Override
    public int[] getPlay() {
        StringBuilder message = new StringBuilder();
        message.append(bringer.getNickname());
        message.append(", select cards to play over ");
        message.append(game.game.getCurrentCard().toString());
        message.append(" or \"draw\" - ");
        message.append(bringer.getHand().toString() + ": ");
        System.out.print(message.toString());

        int[] indices;

        String line = userInput.nextLine();

        if (line.equals("draw"))
            return new int[] { -1 };

        // !Non so perche' ma while (selection.hasNextInt()) { \* ... *\ } funziona per
        // iterare su tutti gli interi (anche senza l'uso di selection.nextLine()) ma
        // continua a prendere input anche dopo l'invio. Pertanto faccio così...
        String[] tokens = line.split(" ");
        indices = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            indices[i] = Integer.parseInt(tokens[i]);
        }

        return indices;
    }

    @Override
    public void makePlay() {
        int[] indices = getPlay();

        if (indices[0] == -1) {
            drawFromDeck();
            return;
        }

        // !Ordiniamo gli indici inversamenti per non dover shiftarli quando modifichiamo la lista.
        Set<Integer> validIndices = new TreeSet<Integer>(Collections.reverseOrder()); 
        List<Card> cardsPlayed = new LinkedList<Card>();

        Card lastCard = game.game.getCurrentCard();
        for (int index : indices) {
            Card cardSelected = bringer.hand.get(index - 1); // !L'input dato parte a contare da 1, quindi
                                                                      // index-1
            cardsPlayed.add(cardSelected);

            if (game.isPlayable(lastCard, cardSelected))
                validIndices.add(index - 1);

            lastCard = cardSelected;
        }

        if (validIndices.isEmpty()) {
            System.out.println("You can't do it now, try again!");
            makePlay();
            return;
        }


        for (Card card : cardsPlayed) {
            playCard(card);
        }
        // !Siccome usiamo un treeSet gli indici sono ordinati e non serve shiftare gli
        // indici
        for (int index : validIndices) {
            bringer.hand.remove(index); // !Gli idici sono già diminuiti di 1 rispetto all'input
        }

        if (game.winCondition(this))
            System.out.println("Well done " + bringer.nickname + ", you won!");
    }
}
