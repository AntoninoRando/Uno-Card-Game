package model.gameEntities;

import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

import model.gameLogic.Action;
import model.gameLogic.Game;
import model.gameObjects.Card;

public class EasyAI extends GameAI {
    public EasyAI(String icon, String nickname) {
        super(icon, nickname);
        //TODO Auto-generated constructor stub
    }

    @Override
    public Entry<Action, Object> chooseFromHand() {
        Entry<Action, Object> choice = Map.entry(Action.FROM_DECK_DRAW, 1);

        Optional<Card> option = getHand().stream().filter(Game.getInstance()::isPlayable).findAny();
        if (option.isPresent())
            choice = Map.entry(Action.FROM_HAND_PLAY_CARD, option.get());

        return choice;
    }

    @Override
    public Entry<Action, Object> chooseFromSelection() {
        // TODO Auto-generated method stub
        return null;
    }
}
