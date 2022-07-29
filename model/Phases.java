package model;

import model.cards.Card;

public class Phases {
    public static final Phase START_TURN = (l, g) -> {
        Player p = g.getPlayer();

        l.events.notify("turnStart", p);
        if (p.isHuman())
            l.events.notify("humanTurn", p);
        else 
            l.events.notify("enemyTurn", p);
            
        p.consumeConditions();
        return true;
    };

    public static final Phase MAKE_CHOICE = (l, g) -> {
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
    };

    public static final Phase PARSE_CHOICE = (l, g) -> {
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
    };

    public static final Phase RESOLVE_CHOICE = (l, g) -> {
        return l.choiceTypes.getOrDefault(l.choiceType, () -> {
            l.events.notify("warning", "Invalid choice!");
            return false;
        }).get();
    };

    public static final Phase END_TURN = (l, g) -> {
        l.events.notify("turnEnd", g.getPlayer());
        g.setTurn(g.getNextPlayer());
        l.choice = null;
        l.choiceType = null;
        l.unoDeclared = false;
        return true;
    };
}
