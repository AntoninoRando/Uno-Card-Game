import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Effect {
    // VARIABLES
    private static List<Effect> instances = new ArrayList<Effect>();

    private GameManager game;
    private int performer; // ! numero (per turno) della persona che attiva l'effetto

    private Map<String, String> triggers = new HashMap<String, String>();

    // CONSTRUCTORS
    public Effect(Map<String, String> effects) {
        triggers = effects;
        instances.add(this);
    }

    public Effect() {
        instances.add(this);
    }

    // METHODS
    private void perform(String effect) {
        switch (effect) {
            case "draw(2, next)": {// !draw(quantity, who)
                Controller to = game.getControllers().get(performer + 1);
                to.drawFromDeck(2);
                break;
            }
            case "draw(4, next)": {
                Controller to = game.getControllers().get(performer + 1);
                to.drawFromDeck(2);
                break;
            }
            case "changeColor(any)": {
                Suit color = new Selector<Suit>().select(Suit.RED, Suit.BLUE, Suit.GREEN, Suit.YELLOW);
                game.putCard(new Card(color, 0));
                break;
            }
            default:
                return;
        }
    }

    /**
     * When a method is called, it can call Effect.checkTrigger and pass as argument
     * the label for the event it represent. Then, this method will perform effects
     * corresponding to triggers activated.
     * 
     * @param eventLabel
     */
    public static void signalGlobal(String eventLabel, GameManager caller, int performer) {
        for (Effect e: instances) {
            e.game = caller;
            e.performer = performer;

            if (e.triggers.containsKey(eventLabel))
                e.perform(e.triggers.get(eventLabel));
        }
    }
    public void signalToThis(String eventLabel, GameManager caller, int performer) {
        game = caller;
        this.performer = performer; 

        if (triggers.containsKey(eventLabel))
            perform(triggers.get(eventLabel));
    }

    public void effectBuilder(String effectText) {
        // !TO-DO
    }
}
