package model.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import events.EventManager;

/**
 * Stores general information about the user info and the data.
 */
public abstract class Info {
    private static final int[] xpGaps = { 5, 8, 15, 21, 24, 28, 31, 35, 39, 50 };
    private static final int nickMaxLength = 22;
    private static final String defaultIcon = "resources\\icons\\night.png";
    private static final String iconsPath = "resources\\icons";
    private static final String defaultNick = "User";
    private static final String cardsPath = "resources\\Cards";

    public static EventManager events = new EventManager();

    /**
     * 
     * @return The max number of characters allowed in the nickname.
     */
    public static int getNickMaxLength() {
        return nickMaxLength;
    }

    /**
     * 
     * @return The default icon to use if the player doesn't have one.
     */
    public static String getDefaultIcon() {
        return defaultIcon;
    }

    /**
     * 
     * @return The default nickname to use if the player doesn't have one.
     */
    public static String getDefaultNick() {
        return defaultNick;
    }

    /**
     * 
     * @param level The level from which to level up.
     * @return The amount of XP need to level up.
     */
    public static int getXpGap(int level) {
        return xpGaps[level % xpGaps.length];
    }

    /**
     * 
     * @return A percentage of the user level progress (from 0 to 100).
     */
    public static double userLevelProgress() {
        return ((double) PlayerData.getXp()) / ((double) getXpGap(PlayerData.getLevel())) * 100.0;
    }

    /**
     * 
     * @return A set of the paths of all icons.
     * @throws IOException
     */
    public static Set<String> allIcons() throws IOException {
        try (Stream<Path> stream = Files.list(Paths.get(iconsPath))) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .map(Path::toString)
                    .collect(Collectors.toSet());
        }
    }

    /**
     * Returns the relative path to the file containing all the cards of the given
     * set.
     */
    public static String getCardsPath(String setName) {
        return cardsPath + "\\" + setName + ".json";
    }
}
