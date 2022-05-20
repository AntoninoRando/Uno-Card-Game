package GameTools;

import CardsTools.Card;
import CardsTools.Suit;

// !Il GameController e i Controller si parlano in modo stretto: hanno accesso 
// a qualsiasi componente del gioco ed insieme devono poter modificare ogni regola 
// (perché gli effetti delle carte devono poterlo fare).
public class GameController {
    // VARIABLES
    // !L'ordine della lista determina l'ordine dei giocatori che giocano, il cui
    // indice è in Game.players.
    //
    // !Manca la variabile turns.
    Game game;
    int[] turnsOrder;
    Controller[] controllers;

    public GameController(Game game, int... turnsOrder) {
        this.game = game;
        this.turnsOrder = turnsOrder;
        controllers = new Controller[turnsOrder.length];

        for (int i = 0; i < turnsOrder.length; i++) {
            Player player = game.getPlayer(turnsOrder[i]);
            Controller playerController = new HumanController(player);
            controllers[i] = playerController;
            playerController.setGame(this);
            // !(riga sopra) Apparte che prende un GameManager, dovrei fare che glielo posso
            // passare nel costruttore, invece di settarlo
        }
    }

    // PHASES
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
    // !Visibilità di default: SOLO NEL PACKAGE (sottoclassi fuori dal package non vedono)
    @Action
    void changeCurrentCard(Card c) {
        game.discardPile.add(c);
    }

    @Action
    // !A pescare dal deck ci pensa il controller, aggiungendo alla mano le carte
    // che rilascia questo metodo.
    Card dealFromDeck(int i) {
        // !Non so se sia corretto usare il riferimento diretto o un getter.
        if (game.deck.isEmpty())
            reshuffle();
        return game.deck.remove(i);
    }

    @Action
    // !Scarta la carta all'inizio perché alla fine sarebbe come giocarla
    void discardCard(Card c) {
        game.discardPile.add(0, c);
    }

    @Action
    void discardFromDeck(int i) {
        discardCard(dealFromDeck(i));
    }

    @Action
    void changeTurnsOrder(int... newOrder) {
        turnsOrder = newOrder;
        updateControllersOrder(newOrder);
    }

    @Action
    void blockTurn(int i) {
        int playerNumber = turnsOrder[i];

        // !Rende il numero del turno negativo. I numeri negativi, quando incontrati,
        // vengono resi di nuovo positivi ma vengono trascurati.
        // !Alternativamente stavo pensando di modificare e farlo con la priorità: chi
        // ha il numero più alto inizia, ecc...
        if (playerNumber >= 0)
            turnsOrder[i] = -playerNumber;
    }

    @Action
    void reshuffle() {
        game.deck.addAll(game.discardPile);
        game.deck.shuffle();
        game.discardPile.clear();
    }

    // METHODS
    // !Potrei tornare boolean se cambia effettivamente l'ordine
    private void updateControllersOrder(int... newOrder) {
        Controller[] controllers = new Controller[newOrder.length];

        for (int i = 0; i < newOrder.length; i++)
            controllers[i] = this.controllers[newOrder[i]];

        this.controllers = controllers;
    }

    protected boolean isPlayable(Card c) {
        Card currentCard = game.getCurrentCard();
        return isPlayable(currentCard, c);
    }

    protected boolean isPlayable(Card c, Card toPlay) {
        // !Dovrei creare un modo di calcolare l'hash code della carta e aggiungere qui
        // un metodo setPlayCondition in cui specifica che hashCode deve avere la carta
        // per essere giocata.
        if (c.getSuit() == Suit.WILD || toPlay.getSuit() == Suit.WILD)
            return true;
        // !Non so se sia giusto usare equals per gli enum.
        return c.getSuit().equals(toPlay.getSuit()) || c.getValue() == toPlay.getValue();
    }

    public void setup() {
        game.deck.shuffle();
        discardFromDeck(0); // Set the first card.

        for (Controller controller : controllers) {
            controller.setGame(this);
            controller.drawFromDeck(5);
        }
    }

    private void playTurn(int i) {
        if (turnsOrder[i] >= 0)
            controllers[i].makePlay();
        else
            turnsOrder[i] = -turnsOrder[i];
    }

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

    public void play() {
        setup();

        while (true) {
            playRound();
        }
    }

    // !Quando un controller controllerà e confermerà che un giocatore ha vinto,
    // finirà il gioco.
    // private void end() {
    // }

    // !Non vorrei dipendesse da un parametro, ma dovrebbe tornare qualcosa che può
    // essere uguagliato per confrontare
    public boolean winCondition(Controller controller) {
        return controller.getBringer().getHand().isEmpty();
    }

    // GETTERS AND SETTERS
    public Controller[] getControllers() {
        return controllers;
    }

    public Game getGame() {
        return game;
    }
}
