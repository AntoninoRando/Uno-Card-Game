package GameTools;

import CardsTools.Card;
import CardsTools.CardGroup;
import CardsTools.Deck;
import Controllers.Player;

public class Game {
    // VARIABLES
    // !Non so se sia corretto renderle protected o se vada aggiunto un getter
    protected Player[] players;
    protected Deck deck;
    protected CardGroup discardPile;

    // CONSTRUCTORS
    public Game(Deck deck, Player... players) {
        this.players = players;
        this.deck = deck;
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