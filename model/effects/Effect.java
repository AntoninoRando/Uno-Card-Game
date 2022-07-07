package model.effects;
import model.cards.Card;
import model.Player;

@FunctionalInterface
public interface Effect {
    public void cast(Player performer, Card source);

    public default Effect andThen(Effect after) {
        return (performer, source) -> {
            this.cast(performer, source);
            after.cast(performer, source);
        };
    }

    public static Effect ofNothing() {
        return (__, ___) -> {};
    }
}