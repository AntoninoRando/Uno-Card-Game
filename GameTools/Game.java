package GameTools;

import CardsTools.Card;
import CardsTools.CardGroup;
import CardsTools.Deck;

public class Game {
    // VARIABLES
    // !Non so se sia corretto renderle protected o se vada aggiunto un getter.
    // !Questo players funziona in maniera particolare. Associa ad ogni Player (e
    // quindi controller) un numero. Il gameController associerà ad ogni numero, che
    // corrisponde quindi a un giocatore, un altro numero (= indice nella lista) che
    // corrisponde al turno in cui giocherà. Cioè la sua posizione nella lista
    // corrisponde al turno in cui giocherà. Tutte queste metainformazioni le salva
    // il GameController, perché il Game (come il Player) contiene solo quello
    // superficiale "che il giocatore vede"
    protected Player[] players;
    protected Deck deck;
    protected CardGroup discardPile;

    // CONSTRUCTORS
    public Game(Deck deck, Player... players) {
        this.players = players;
        this.deck = deck;
        discardPile = new CardGroup();
    }

    // GETTERS AND SETTERS
    public Card getCurrentCard() {
        return discardPile.getLast();
    }

    public Player getPlayer(int i) {
        return players[i];
    }

    public Player[] getAllPlayers() {
        return players;
    }
}

// !Voglio che Game, come Player, sia solo una classe contenitore degli elementi
// che poi il GameManager, così come i controller (a questo punto sarebbe
// sensato chiamarlo GameController), controllano. Questo renderebbe ancora più
// sensato il fatto che il GameManage/controller lavori solo sui controller.