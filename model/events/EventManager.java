package model.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javafx.util.Pair;

// TODO gestire meglio gli eventi del tipo che se ci sono due eventi con lo stesso tipo vengono accorpati in un solo metodo da eseguire
public class EventManager {
    /* OBSERVER PATTERN */
    /* ---------------- */
    private Map<String, List<EventListener>> listeners;

    public EventManager() {
        listeners = new HashMap<>();
    }

    // TODO aggiungere che ci si può iscrivere per ascoltare categorie e non
    // specifici eventi
    public void subscribe(EventListener listener, String... eventLabels) {
        for (String label : eventLabels) {
            listeners.putIfAbsent(label, new ArrayList<>());
            listeners.get(label).add(listener);
            listeners.get(label).sort((e1, e2) -> e1.compareTo(label, e2));
        }
    }

    public void unsubscribe(String eventLabel, EventListener listener) {
        listeners.get(eventLabel).remove(listener);
    }

    public void notify(String eventLabel, Object data) {
        changeState(eventLabel);

        LinkedList<Pair<String, EventListener>> toNotify = new LinkedList<>();

        if (listeners.containsKey(eventLabel))
            listeners.get(eventLabel).forEach(l -> toNotify.add(new Pair<String, EventListener>(eventLabel, l)));

        for (String complexEvent : getComplexEventsVerified())
            listeners.get(complexEvent).forEach(l -> toNotify.add(new Pair<String, EventListener>(complexEvent, l)));

        toNotify.sort((pair1, pair2) -> -Integer.compare(pair1.getValue().getEventPriority(pair1.getKey()),
                pair2.getValue().getEventPriority(pair2.getKey())));
        toNotify.forEach(pair -> pair.getValue().update(pair.getKey(), data));
    }

    public void notify(String eventLabel, Object... data) {
        changeState(eventLabel);

        if (listeners.containsKey(eventLabel))
            listeners.get(eventLabel).forEach(e -> e.update(eventLabel, data));

        for (String complexEvent : getComplexEventsVerified()) {
            listeners.get(complexEvent).forEach(e -> e.update(complexEvent, data));
        }
    }

    /* ------------------------------ */

    private Map<String, String> state = new HashMap<>();

    private static String getCategoryOf(String label) {
        switch (label) {
            case "enemyTurn":
            case "humanTurn":
            case "gameStart":
                return "turn";
            default:
                return "unclassified";
        }
    }

    private void changeState(String eventLabel) {
        state.put(getCategoryOf(eventLabel), eventLabel);
    }

    private String[] getComplexEventsVerified() {
        return listeners.keySet().stream()
                .filter(s -> s.contains(" "))
                .filter(s -> Stream.of(s.split(" ")).allMatch(str -> state.containsValue(str)))
                .toArray(size -> new String[size]);
    }
}