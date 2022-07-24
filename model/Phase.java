package model;

import java.util.function.BiFunction;

import model.cards.Card;

public enum Phase {
    START_TURN((l, g) -> {
        l.events.notify("turnStart", g.getPlayer());
        if (g.getPlayer().isHuman())
            l.events.notify("humanTurn", g.getPlayer());
        else 
            l.events.notify("enemyTurn", g.getPlayer());
        g.getPlayer().consumeConditions();
        return true;
    }),
    MAKE_CHOICE((l, g) -> {
        if (g.getPlayer().isHuman())
            synchronized (l) {
                try {
                    l.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        // enemy decision
        else
            g.getPlayer().getHand().stream().filter(g::isPlayable).findAny()
                    .ifPresentOrElse(c -> l.choice = c, () -> l.choice = "draw");
        return true;
    }),
    PARSE_CHOICE((l, g) -> {
        if (l.choice instanceof Card)
            l.choiceType = "card";
        else if (l.choice instanceof String) {
            switch ((String) l.choice) {
                case "draw":
                    l.choiceType = "draw";
                    break;
                case "unoDeclared":
                    l.choiceType = "unoDeclared";
                    break;
                default:
                    throw new Error(
                            "The Loop said: someone notifyed me \"" + l.choice + "\" but I can't handle that event!");
            }
        } else if (l.choice instanceof Integer) {
            l.choiceType = "cardPosition";
        }
        return true;
    }),
    RESOLVE_CHOICE((l, g) -> {
        return l.choiceTypes.getOrDefault(l.choiceType, () -> {
            l.events.notify("warning", "Invalid choice!");
            return false;
        }).get();
    }),
    END_TURN((l, g) -> {
        l.events.notify("turnEnd", g.getPlayer());
        g.setTurn(g.getTurn() + 1);
        l.choice = null;
        l.choiceType = null;
        l.unoDeclared = false;
        return true;
    });

    public BiFunction<Loop, Game, Boolean> execution;

    private Phase(BiFunction<Loop, Game, Boolean> execution) {
        this.execution = execution;
    }
}
