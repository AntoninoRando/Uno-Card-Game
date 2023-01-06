package model.gameEntities;

public abstract class Enemies {
    public static final GameAI VIEGO = new EasyAI("resources\\icons\\pirate.png", "Viego");
    public static final GameAI JINX = new CheaterAI("resources\\icons\\blood.png", "Jinx");
    public static final GameAI ZOE = new CheaterAI("resources\\icons\\queen.png", "Zoe");
    public static final GameAI XAYAH = new EasyAI("resources\\icons\\night.png", "Xayah");
}
