package model.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import events.EventManager;

public abstract class Info {
    private static final int[] xpGaps = { 5, 8, 15, 21, 24, 28, 31, 35, 39, 50 };
    private static final int nickMaxLength = 22;
    private static final String defaultIcon = "resources\\icons\\night.png";
    private static final String iconsPath = "resources\\icons";
    private static final String defaultNick = "User";
    private static final String cardsPath = "resources\\Cards";

    public static EventManager events = new EventManager();

    public static int getNickMaxLength() {
        return nickMaxLength;
    }

    public static String getDefaultIcon() {
        return defaultIcon;
    }

    public static String getDefaultNick() {
        return defaultNick;
    }

    public static int getXpGap(int level) {
        return xpGaps[level % xpGaps.length];
    }

    public static double userLevelProgress() {
        return ((double) PlayerData.getXp()) / ((double) getXpGap(PlayerData.getLevel())) * 100.0;
    }

    public static Set<String> allIcons() throws IOException {
        try (Stream<Path> stream = Files.list(Paths.get(iconsPath))) {
            return stream
              .filter(file -> !Files.isDirectory(file))
              .map(Path::toString)
              .collect(Collectors.toSet());
        }
    }

    public static String getCardsPath(String setName) {
        return cardsPath + "\\" + setName + ".json";
    }
}
