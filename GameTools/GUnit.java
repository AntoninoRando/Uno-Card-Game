package GameTools;

import CardsTools.Card;

public abstract class GUnit {
    public static Game game;
    private static int cardInput;

    // !Attualmente è così che funziona: il bottone ha un actionListener: quando
    // viene cliccato chiama questo metodo e manda un segnale al CardListener, che
    // segnalerà che è stata giocata una carta e farà aggiornare la carta a terra.
    public static void giveInput(Controller controller, Card card) {
        int index = controller.getBringer().getHand().indexOf(card);
        cardInput = index+1;
    }

    public static int getInput() {
        return cardInput;
    }
}
