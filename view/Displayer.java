package view;


import java.util.HashSet;
import java.util.Set;

import events.EventListener;
import events.EventType;
import javafx.application.Application;

public abstract class Displayer extends Application implements EventListener {
    private Set<EventType> eventsListening;

    public Displayer self() {
        return this;
    }

    public Displayer(Set<EventType> events) {
        eventsListening = events;
    }

    public Displayer(EventType... events) {
        eventsListening = new HashSet<>();
        for (EventType e : events)
            addEventToListen(e);
    }

    public Set<EventType> getEventsListening() {
        return eventsListening;
    }

    public void addEventToListen(EventType eventType) {
        eventsListening.add(eventType);
    }
}
