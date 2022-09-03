package view;


import java.util.HashSet;
import java.util.Set;

import events.EventListener;
import javafx.application.Application;

public abstract class Displayer extends Application implements EventListener {
    private Set<String> eventsListening;

    public Displayer self() {
        return this;
    }

    public Displayer(Set<String> events) {
        eventsListening = events;
    }

    public Displayer(String... events) {
        eventsListening = new HashSet<>();
        for (String e : events)
            addEventToListen(e);
    }

    public Set<String> getEventsListening() {
        return eventsListening;
    }

    public void addEventToListen(String eventType) {
        eventsListening.add(eventType);
    }
}
