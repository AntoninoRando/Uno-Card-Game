package EffectsTools;

import java.util.ArrayList;
import java.util.List;

import CardsTools.Card;
import GameTools.GameManager;

public abstract class Observants { // !Potrebbe estendere cardGroup (ma non è static)
    private static List<Card> all = new ArrayList<Card>();

    public static boolean add(Card c) {
        return all.add(c);
    }

    public static boolean remove(Card c) {
        return all.remove(c);
    }

    public static List<Card> get() {
        return all;
    }

    // !Dovrebbe tornare true se almeno una carta è stata attivata
    public static void trigger(String eventLabel, GameManager caller, int performer) {
        all.forEach(c -> c.getEffect().signalToThis(eventLabel, caller, performer));
    }
}
