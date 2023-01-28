package model.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/* --- Mine ------------------------------- */

import events.Event;
import events.EventListener;
import model.CUModel;

/**
 * A class containig player's info. It also contains static info about the user.
 */
public abstract class UserData implements EventListener {
    /* --- Fields ----------------------------- */

    public static final String DEFAULT_NICKNAME = "User";
    // public static final int NICKNAME_MAX_SIZE = 22;
    public static final String DEFAULT_ICON = "resources\\icons\\night.png";
    public static final int[] XP_GAPS = { 5, 8, 15, 21, 24, 28, 31, 35, 39, 50 };
    public static final EventListener EVENT_LISTENER = new EventListener() {
        @Override
        public void update(Event event, HashMap<String, Object> data) {
            switch (event) {
                case INFO_CHANGE:
                    data.keySet().forEach(key -> {
                        switch (key) {
                            case "nickname":
                                setNickname((String) data.get("nickname"));
                                break;
                            case "icon":
                                setIcon((String) data.get("icon"));
                                break;
                        }
                    });
                    break;
                case INFO_RESET:
                    nickname = DEFAULT_NICKNAME;
                    level = 1;
                    xp = 0;
                    games = 0;
                    wins = 0;
                    setIcon(DEFAULT_ICON); // Used also to notify
                    break;
                default:
                    throwUnsupportedError(event, data);
                    break;
            }
        }
    };

    private static String nickname;
    private static String icon;
    private static int level;
    private static int xp;
    private static int games;
    private static int wins;

    /* ---.--- Getters and Setters ------------ */

    public static String getNickname() {
        return nickname;
    }

    public static void setNickname(String nickname) {
        UserData.nickname = nickname;
        CUModel.communicate(Event.INFO_CHANGE, wrapData());
    }

    public static String getIcon() {
        return icon;
    }

    public static void setIcon(String icon) {
        UserData.icon = icon;
        CUModel.communicate(Event.INFO_CHANGE, wrapData());
    }

    public static int getLevel() {
        return level;
    }

    public static int getXp() {
        return xp;
    }

    public static int getGames() {
        return games;
    }

    public static int getWins() {
        return wins;
    }

    /* --- Body ------------------------------- */

    public static HashMap<String, Object> wrapData() {
        HashMap<String, Object> data = new HashMap<>();

        data.put("nickname", getNickname());
        data.put("icon", getIcon());
        data.put("level", getLevel());
        data.put("xp", getXp());
        data.put("xp-gap", XP_GAPS[level-1 % XP_GAPS.length]);
        data.put("games", getGames());
        data.put("wins", getWins());

        return data;
    }

    /**
     * Reads the input file to extract user info and initialize this class fields.
     * 
     * @param filePathname The path to the file containing all user info.
     */
    public static void load(String filePathname) {
        Stream<Consumer<String>> readStream = Stream.of(
                line -> nickname = line,
                line -> icon = line,
                line -> level = Integer.parseInt(line),
                line -> xp = Integer.parseInt(line),
                line -> games = Integer.parseInt(line),
                line -> wins = Integer.parseInt(line));

        try (Scanner sc = new Scanner(new FileInputStream(filePathname))) {
            readStream.forEach(consumer -> consumer.accept(sc.nextLine()));
        } catch (IOException e) {
        }

        CUModel.communicate(Event.INFO_CHANGE, wrapData());
    }

    /**
     * Writes this fileds values into the input file.
     * 
     * @param filePathname The path to the file that will contain new user info.
     */
    public static void write(String filePathname) {
        Stream<String> writeStream = Stream.concat(
                Stream.of(nickname, icon),
                Stream.of(level, xp, games, wins).map(x -> Integer.toString(x)));

        String newText = writeStream.collect(Collectors.joining("\n"));

        try (FileOutputStream fos = new FileOutputStream(filePathname)) {
            fos.write(newText.getBytes());
        } catch (IOException e) {
        }
    }

    /**
     * Resets all this fields.
     */
    public static void reset() {
        nickname = DEFAULT_NICKNAME;
        icon = DEFAULT_ICON;
        level = 0;
        xp = 0;
        games = 0;
        wins = 0;

        CUModel.communicate(Event.INFO_CHANGE, wrapData());
    }

    /**
     * Adds the input number of xp.
     * 
     * @param quantity Number of xp <em>earned</em>.
     */
    public static void addXp(int quantity) {
        while (quantity > 0) {
            int gap = XP_GAPS[level-1 % XP_GAPS.length];

            int toAdd = Integer.min(quantity, gap - xp);
            xp += toAdd;
            quantity -= toAdd;

            if (xp == gap) {
                level++;
                xp = 0;
            }
        }

        CUModel.communicate(Event.INFO_CHANGE, wrapData());
    }

    /**
     * Marks another game played, and eventually won.
     * 
     * @param win If the user won the game played.
     */
    public static void addGamePlayed(boolean win) {
        games++;
        if (win)
            wins++;

        CUModel.communicate(Event.INFO_CHANGE, wrapData());
    }
}
