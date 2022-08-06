package model.gameLogic;

public abstract class Phases {
    public static final Phase START_TURN = (l, g) -> {
        Player p = g.getPlayer();

        Loop.events.notify("turnStart", p);
        if (p.isHuman())
            Loop.events.notify("humanTurn", p);
        else 
            Loop.events.notify("enemyTurn", p);
            
        p.consumeConditions();
        return true;
    };

    public static final Phase MAKE_CHOICE = (l, g) -> {
        if (g.getPlayer().isHuman())
            synchronized (l) {
                try {
                    l.wait();
                } catch (InterruptedException e) {
                }
            }
        // enemy decision
        else
            g.getPlayer().getHand().stream().filter(g::isPlayable).findAny()
                    .ifPresentOrElse(c -> Loop.choice = c, () -> Loop.choice = "draw");
        return true;
    };

    public static final Phase PARSE_CHOICE = (l, g) -> {
        if (Loop.choice instanceof Card)
            Loop.choiceType = "card";
        else if (Loop.choice instanceof String) {
            switch ((String) Loop.choice) {
                case "draw":
                    Loop.choiceType = "draw";
                    break;
                case "unoDeclared":
                    Loop.choiceType = "unoDeclared";
                    break;
                default:
                    throw new Error(
                            "The Loop said: someone notifyed me \"" + Loop.choice + "\" but I can't handle that event!");
            }
        } else if (Loop.choice instanceof Integer) {
            Loop.choiceType = "cardPosition";
        }
        return true;
    };

    public static final Phase RESOLVE_CHOICE = (l, g) -> {
        return Loop.choiceTypes.getOrDefault(Loop.choiceType, () -> {
            Loop.events.notify("warning", "Invalid choice!");
            return false;
        }).get();
    };

    public static final Phase END_TURN = (l, g) -> {
        Loop.events.notify("turnEnd", g.getPlayer());
        g.setTurn(g.getNextPlayer());
        Loop.choice = null;
        Loop.choiceType = null;
        Loop.unoDeclared = false;
        return true;
    };
}
