package model.gameEntities;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import model.gameLogic.Action;
import model.gameLogic.Game;
import model.gameObjects.Card;
import model.gameObjects.Suit;

public class CheaterAI extends GameAI {
    public CheaterAI(String icon, String nickname) {
        super(icon, nickname);
    }

    @Override
    public Entry<Action, Object> chooseFromHand() {
        Game game = Game.getInstance();
        Entry<Action, Object> choice = Map.entry(Action.FROM_DECK_DRAW, 1);

        List<Card> options = getHand().stream().filter(game::isPlayable).collect(Collectors.toList());

        if (options.size() == 0)
            return choice;

        Player user = Stream.of(game.getPlayers()).filter(p -> !(p instanceof GameAI)).findAny().get();
        List<Suit> frequency = user.getHand().stream()
            .map(c -> c.getSuit())
            .collect(Collectors.toMap(
                s -> s, // Key mapper
                s -> 1,  // Value mapper
                (oldOccurency, one) -> oldOccurency + one)) // Merge function
            .entrySet().stream() // Stream of entries (key and value)
            .sorted((entry1, entry2) -> entry1.getValue() - entry2.getValue()) // first = most frequent
            .map(entry -> entry.getKey()) // map to suits
            .collect(Collectors.toList());
        
        Collections.sort(options, Comparator.comparing(c -> frequency.indexOf(c.getSuit())));

        choice = Map.entry(Action.FROM_HAND_PLAY_CARD, options.get(0));

        return choice;
    }

    @Override
    public Entry<Action, Object> chooseFromSelection() {
        // TODO Auto-generated method stub
        return null;
    }
}
