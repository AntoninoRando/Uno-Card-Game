package view;

import java.io.File;
import java.util.HashMap;

/**
 * Implements the <em>Flyweight</em> pattern as <b>FlyweightFactory</b>.
 * <p>
 * A group of sprites that can be easily fetched.
 */
public abstract class SpriteFactory {
    private static final String CARDS_PATH = "resources/images/cards";
    private static final String BUTTONS_PATH = "resources/images/buttons";
    private static final String AVATARS_PATH = "resources/images/avatars";
    private static final HashMap<String, Sprite> sprites = new HashMap<>();

    /**
     * The general-sprite getter: it can returns any image file as a
     * <code>Sprite</code> object.
     * 
     * @param filePath The image file path.
     * @return The <code>Sprite</code> object of the image contained in the file.
     */
    public static Sprite getSprite(String filePath) {
        if (!sprites.containsKey(filePath))
            sprites.put(filePath, new Sprite(filePath, filePath));
        return sprites.get(filePath);
    }

    
    /** 
     * @param filePath
     * @param name
     * @return Sprite
     */
    private static Sprite getSprite(String filePath, String name) {
        if (!sprites.containsKey(filePath))
            sprites.put(filePath, new Sprite(filePath, name));
        return sprites.get(filePath);
    }

    /**
     * The card-sprite getter: it returns only card image file as a
     * <code>Sprite</code> object.
     * 
     * @param cardString The string representation of the card.
     * @return The <code>Sprite</code> object of the image contained in the file.
     */
    public static Sprite getCardSprite(String cardString) {
        return getSprite(CARDS_PATH + "/" + cardString + ".png", cardString);
    }

    /**
     * The button-sprite getter: it returns only button image file as a
     * <code>Sprite</code> object.
     * 
     * @param buttonName The name of the button (i.e., its functionality).
     * @return The <code>Sprite</code> object of the image contained in the file.
     */
    public static Sprite getButtonSprite(String buttonName) {
        return new Sprite(BUTTONS_PATH + "/" + buttonName + ".png", buttonName);
        // Don't save the button image cause there are only few buttons and they are
        // unique.
    }

    /**
     * The avatar-sprite getter: it returns only avatar image file as a
     * <code>Sprite</code> object.
     * 
     * @param avatarName The name of the avatar.
     * @return The <code>Sprite</code> object of the image contained in the file.
     */
    public static Sprite getAvatarSprite(String avatarName) {
        return getSprite(AVATARS_PATH + "/" + avatarName + ".png", avatarName);
    }

    
    /** 
     * @return Sprite[]
     */
    public static Sprite[] getAllAvatars() {
        String[] files = new File(AVATARS_PATH).list();
        Sprite[] sprites = new Sprite[files.length];

        for (int i = 0; i < files.length; i++) {
            String file = files[i];
            String name = file.substring(0, file.length() - 4); // removing .png; 
            sprites[i] = getSprite(AVATARS_PATH + "/" + file, name);
        }
        return sprites;
    }
}
