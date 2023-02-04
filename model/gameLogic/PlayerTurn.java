package model.gameLogic;

import java.util.HashMap;
import java.util.Map.Entry;

/* --- JUno ------------------------------- */

import events.Event;

import model.cards.Card;
import model.players.Player;

/**
 * The game state in which the player will resolve its turn. After that, there
 * will
 * be either the TransitionState or the CardTurn state.
 */
public class PlayerTurn implements GameState {
    /* --- State ------------------------------ */

    /**
     * The context.
     */
    private Player player;
    /**
     * The context.
     */
    private Game game;

    /**
     * Sets the context for this state.
     * 
     * @param player The player player that will take the turn.
     * @param game   The current match in which the player is taking its turn.
     */
    public void setContext(Player player, Game game) {
        this.player = player;
        this.game = game;
    }

    @Override
    public void resolve() {
        game.notifyToCU(Event.TURN_START, player.getData());

        Card cardPlayed = null;
        boolean turnEnded = false;
        boolean unoNeed = player.getHand().size() == 2;

        while (!turnEnded) {
            Entry<Action, Object> choice = player.chooseFrom(player.getHand().toArray(Card[]::new), game.getPlayCondition());
            switch (choice.getKey()) {
                case FROM_DECK_DRAW:
                    int quantity = (int) choice.getValue();
                    game.dealFromDeck(player, quantity);
                    turnEnded = true;
                    break;
                case FROM_HAND_PLAY_CARD:
                    cardPlayed = (Card) choice.getValue();
                    game.playerCard(player, cardPlayed);

                    if (unoNeed) {
                        HashMap<String, Object> dataUno = new HashMap<>();
                        dataUno.put("said", false);
                        game.notifyToCU(Event.UNO_DECLARED, dataUno);
                        game.dealFromDeck(player, 2);
                    }

                    turnEnded = true;
                    break;
                case INVALID:
                    HashMap<String, Object> dataInvalid = new HashMap<>();
                    dataInvalid.put("card-ID", ((Card) choice.getValue()).getTag());
                    game.notifyToCU(Event.INVALID_CARD, dataInvalid);
                    break;
                case SAY_UNO:
                    if (!unoNeed)
                        break;
                    unoNeed = false;
                    HashMap<String, Object> dataUno = new HashMap<>();
                    dataUno.put("said", true);
                    game.notifyToCU(Event.UNO_DECLARED, dataUno);
                    break;
                default:
                    throw new Error("player toke its turn with an unimplemented choice: " + choice.getKey());
            }
        }
        // Change state.
        CardTurn nextState = new CardTurn();
        nextState.setContext(cardPlayed, game);
        game.changeState(nextState);
    }
}
