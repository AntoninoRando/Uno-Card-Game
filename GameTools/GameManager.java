package GameTools;
import java.util.List;

import CardsTools.Card;
import CardsTools.CardGroup;
import CardsTools.Deck;
import Controllers.Controller;
import Controllers.Player;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * This class manage the game. A game is an istance of GameManager.
 */
public class GameManager {
    // VARIABLES
    private Deck drawingDeck;
    private CardGroup discardPile = new CardGroup();
    private Card terrainCard;
    private int turns;

    // We use LinkedList to define the order in which players play.
    private List<Controller> controllers = new LinkedList<Controller>();

    // CONSTRUCTORS
    public GameManager(Deck drawingDeck, List<Controller> controllers) {
        this.drawingDeck = drawingDeck;
        this.controllers = controllers;
    }

    public GameManager(Deck drawingDeck, Controller... controllers) {
        this.drawingDeck = drawingDeck;
        this.controllers = Arrays.asList(controllers);
    }

    // PHASES
    @Phase
    public void setup() {
        drawingDeck.shuffle();
        terrainCard = this.drawingDeck.remove(0);
        turns = 1;

        for (Controller controller : controllers) {
            controller.setGame(this);
            controller.drawFromDeck(5);
        }
    }

    @Phase
    public void playTurns() {
        for (Controller controller : controllers)
            controller.makePlay();
        turns++;
    }

    @Phase
    public void playGame() {
        setup();
        while (!controllers.isEmpty()) {
            playTurns();
        }
    }

    // METHODS
    public boolean reShuffle() {
        boolean hasChanghed = drawingDeck.addAll(discardPile);
        drawingDeck.shuffle();
        discardPile.clear();
        return hasChanghed;
    }

    public Card drawFromDeck() {
        if (drawingDeck.isEmpty())
            reShuffle();
        return drawingDeck.remove(0);
    }

    public void putCard(Card card) {
        discardPile.add(terrainCard);
        terrainCard = card;
    }

    public boolean playCard(Card card) {
        if (!card.isPlayable(terrainCard))
            return false;

        putCard(card);
        return true;
    }

    // !Questo metodo fa schifo
    public boolean checkWin(Controller controller) {
        Player bringer = controller.getBringer();

        if (bringer.getHand().size() != 0)
            return false;

        System.out.println("Well done " + bringer.getNickname() + ", you won!");
        // !Anche se da questo punto in poi la partita finisce, questa istruzione darà
        // errore (penso perché la lista che gli passo è immutabile)
        controllers.clear(); // Game will end because controllers is empty
        return true;

    }

    // GETTERS AND SETTERS
    public Deck getDrawingDeck() {
        return drawingDeck;
    }

    // !Potrei fare che ti da la prima carta della discard pile, e che quindi non
    // eiste la variabile terrainCard
    public Card getTerrainCard() {
        return terrainCard;
    }

    public int getTurns() {
        return turns;
    }

    public List<Controller> getControllers() {
        return controllers;
    }

    // METHODS AND VARIABLES USED FOR EFFECTS
    public void changeTurnsOrder(int... order) {
        List<Controller> newOrder = new LinkedList<Controller>();
        for (int index : order)
            newOrder.add(controllers.get(index)); 
        controllers = newOrder; // !Ho paura di l'errore che cambio la lista mentre itero.
    }
}