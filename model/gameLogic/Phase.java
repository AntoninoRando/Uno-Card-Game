package model.gameLogic;

import java.util.function.BiFunction;


public interface Phase extends BiFunction<Loop, Game, Boolean> {
}