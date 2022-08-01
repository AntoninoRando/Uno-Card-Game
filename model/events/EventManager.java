package model.events;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

// TODO gestire meglio gli eventi del tipo che se ci sono due eventi con lo stesso tipo vengono accorpati in un solo metodo da eseguire
public class EventManager {
    /* OBSERVER PATTERN */
    /* ---------------- */
    private Map<String, List<EventListener>> listeners; // TODO sarebbe più comodo invertire chiave e valore

    public EventManager() {
        listeners = new HashMap<>();
    }

    // TODO aggiungere che ci si può iscrivere per ascoltare categorie e non specifici eventi
    public void subscribe(String eventLabel, EventListener listener) {
        listeners.putIfAbsent(eventLabel, new LinkedList<>());
        listeners.get(eventLabel).add(listener);
    }

    public void subscribe(EventListener listener, String... eventLabels) {
        for (String label : eventLabels)
            subscribe(label, listener);
    }

    public void unsubscribe(String eventLabel, EventListener listener) {
        listeners.get(eventLabel).remove(listener);
    }

    public void notify(String eventLabel, Object data) {
        changeState(eventLabel);

        if (listeners.containsKey(eventLabel))
            listeners.get(eventLabel).forEach(e -> e.update(eventLabel, data));
        
        for (String complexEvent : getComplexEventsVerified()) {
            listeners.get(complexEvent).forEach(e -> e.update(complexEvent, data));
        }
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

    private void changeState(String eventLabel) {
        state.put(Event.getCategoryOf(eventLabel), eventLabel);
    }

    private String[] getComplexEventsVerified() {
        return listeners.keySet().stream()
                .filter(s -> s.contains(" "))
                .filter(s -> Stream.of(s.split(" ")).allMatch(str -> state.containsValue(str)))
                .toArray(size -> new String[size]);
    }
}