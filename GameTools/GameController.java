package GameTools;

import CardsTools.Card;
import CardsTools.Suit;
import Controllers.Controller;
import Controllers.HumanController;
import Controllers.Player;

public class GameController {
    // VARIABLES
    // !L'ordine della lista determina l'ordine dei giocatori che giocano, il cui
    // indice è in Game.players.
    //
    // !Manca la variabile turns.
    private Game game;
    private int[] turnsOrder;
    private Controller[] controllers;

    public GameController(Game game, int... turnsOrder) {
        this.game = game;
        this.turnsOrder = turnsOrder;
        controllers = new Controller[turnsOrder.length];

        for (int i = 0; i < turnsOrder.length; i++) {
            Player player = game.getPlayer(turnsOrder[i]);
            Controller playerController = new HumanController(player);
            controllers[i] = playerController;
            // playerController.setGame(this);
            // !(riga sopra) Apparte che prende un GameManager, dovrei fare che glielo posso
            // passare nel costruttore, invece di settarlo
        }
    }

    // PHASES
    @Phase
    private void playTurn(int i) {
        if (turnsOrder[i] >= 0)
            controllers[i].makePlay();
        else
            turnsOrder[i] = -turnsOrder[i];
    }

    @Phase
    // !Non è chiaro cosa sia un round in realtà, perché non esiste come concetto su
    // Uno.
    private void playRound() {
        // !Uso un for normale invece che un forEach così che se cambia il turnsOrder
        // non dà errore.
        for (int i = 0; i < turnsOrder.length; i++) {
            int playerNumber = turnsOrder[i];
            playTurn(playerNumber);
        }
    }

    @Phase
    private void turnStart() {
    }

    @Phase
    private void decisionMaking() {
    }

    @Phase
    private void cardDecision() {
    }

    @Phase
    private void cardPlay() {
    }

    @Phase
    private void cardDraw() {
    }

    @Phase
    private void turnEnd() {
    }

    // ACTIONS
    @Action
    private void changeCurrentCard(Card c) {
        game.discardPile.add(c);
    }

    @Action
    // !A pescare dal deck ci pensa il controller, aggiungendo alla mano le carte
    // che rilascia questo metodo.
    private Card dealFromDeck(int i) {
        // !Non so se sia corretto usare il riferimento diretto o un getter.
        return game.deck.remove(i);
    }

    @Action
    private void changeTurnsOrder(int... newOrder) {
        turnsOrder = newOrder;
        updateControllersOrder(newOrder);
    }

    @Action
    private void blockTurn(int i) {
        int playerNumber = turnsOrder[i];

        // !Rende il numero del turno negativo. I numeri negativi, quando incontrati,
        // vengono resi di nuovo positivi ma vengono trascurati.
        // !Alternativamente stavo pensando di modificare e farlo con la priorità: chi
        // ha il numero più alto inizia, ecc...
        if (playerNumber >= 0)
            turnsOrder[i] = -playerNumber;
    }

    // METHODS
    // !Potrei tornare boolean se cambia effettivamente l'ordine
    private void updateControllersOrder(int... newOrder) {
        Controller[] controllers = new Controller[newOrder.length];

        for (int i = 0; i < newOrder.length; i++)
            controllers[i] = this.controllers[newOrder[i]];

        this.controllers = controllers;
    }

    private boolean isPlayable(Card c) {
        // !Dovrei creare un modo di calcolare l'hash code della carta e aggiungere qui
        // un metodo setPlayCondition in cui specifica che hashCode deve avere la carta
        // per essere giocata.
        Card currentCard = game.getCurrentCard();

        if (c.getSuit() == Suit.WILD || currentCard.getSuit() == Suit.WILD) 
            return true;
        // !Non so se sia giusto usare equals per gli enum.
        return c.getSuit().equals(currentCard.getSuit()) || c.getValue() == currentCard.getValue();
    }
}
