package view;

import java.util.HashMap;

/**
 * Implements the <em>Flyweight</em> pattern as <b>FlyweightFactory</b>.
 */
public abstract class SpriteFactory {
    private static HashMap<String, Sprite> sprites = new HashMap<>();

    public static Sprite getSprite(String filePath) {
        if (!sprites.containsKey(filePath))
            sprites.put(filePath, new Sprite(filePath));
        return sprites.get(filePath);
    }
}
