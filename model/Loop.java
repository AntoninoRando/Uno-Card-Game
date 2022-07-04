package model;

import model.events.EventManager;

public class Loop {
    public EventManager events;
    private Game g;

    public void play() {
        Game.reset();
        g = Game.getInstance();
    }

    private void turnStart() {
    }

    private Object makeChoice() throws InterruptedException {
        Player p = g.getPlayer();
        if (p.isHuman)
            return events.waitFor("DecisionMade" + p.ID);
        else
            return 1; //TO-DO enemy decision!
    }

    private Object parseChoice() {
        // TO-DO!
        return 1;
    }

    private void resolveChoice() {
    }

    private void turnEnd() {
    }

}
