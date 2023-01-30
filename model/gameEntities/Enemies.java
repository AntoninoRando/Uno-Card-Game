package model.gameEntities;

public abstract class Enemies {
    public static final GameAI VIEGO = new EasyAI("pirate", "Viego");
    public static final GameAI JINX = new CheaterAI("blood", "Jinx");
    public static final GameAI ZOE = new CheaterAI("queen", "Zoe");
    public static final GameAI XAYAH = new EasyAI("night", "Xayah");
}
