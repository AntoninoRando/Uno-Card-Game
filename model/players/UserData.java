package model.players;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/* --- JUno ------------------------------- */

import events.Event;
import events.EventListener;

import model.CUModel;

/**
 * The manager for all the user profile info. It is also responsible of updating
 * the profile, writing the info and loading the info.
 */
public abstract class UserData {
    /* --- Fields ----------------------------- */

    private static final int NICKNAME_MAX_SIZE = 22;
    private static final String DEFAULT_NICKNAME = "User";
    private static final String DEFAULT_ICON = "night";
    private static final int[] XP_GAPS = { 5, 8, 15, 21, 24, 28, 31, 35, 39, 50 };
    public static final EventListener EVENT_LISTENER = (event, data) -> update(event, data);

    private static String nickname;
    private static String icon;
    private static int level;
    private static int xp;
    private static int games;
    private static int wins;

    /* ---.--- Getters and Setters ------------ */

    public static String getNickname() {
        return UserData.nickname;
    }

    /**
     * Sets the new nickname only if it pass all validity checks. Then, communicates
     * this change (if it happened).
     * 
     * @param nickname The new nickname that may be set as the player new nickname.
     */
    private static void setNickname(String nickname) {
        if (Stream.of(Enemy.values()).map(en -> en.toString()).anyMatch(invalid -> invalid.equals(nickname)))
            return;

        if (nickname.length() > NICKNAME_MAX_SIZE)
            return;

        if (nickname.length() == 0)
            return;

        UserData.nickname = nickname;
        CUModel.communicate(Event.INFO_CHANGE, wrapData());
    }

    public static String getIcon() {
        return UserData.icon;
    }

    /**
     * Sets the new icon and communicates the change.
     * 
     * @param icon The icon name.
     */
    private static void setIcon(String icon) {
        UserData.icon = icon;
        CUModel.communicate(Event.INFO_CHANGE, wrapData());
    }

    public static int getLevel() {
        return UserData.level;
    }

    public static int getXp() {
        return UserData.xp;
    }

    public static int getGames() {
        return UserData.games;
    }

    public static int getWins() {
        return UserData.wins;
    }

    /* --- Body ------------------------------- */

    /**
     * Wraps all the user profile data in a dictionary that associate each field
     * with its value, and then returns it.
     * 
     * @return A map field-value: it contains the nickname, icon, level, xp, xp-gap,
     *         games, and wins od the player.
     */
    public static HashMap<String, Object> wrapData() {
        HashMap<String, Object> data = new HashMap<>();

        data.put("nickname", getNickname());
        data.put("icon", getIcon());
        data.put("level", getLevel());
        data.put("xp", getXp());
        data.put("xp-gap", XP_GAPS[level - 1 % XP_GAPS.length]);
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
     * Resets all the user info to their default values.
     */
    public static void reset() {
        UserData.nickname = DEFAULT_NICKNAME;
        UserData.icon = DEFAULT_ICON;
        UserData.level = 1;
        UserData.xp = 0;
        UserData.games = 0;
        UserData.wins = 0;

        CUModel.communicate(Event.INFO_CHANGE, wrapData());
    }

    /**
     * Adds the input number of xp.
     * 
     * @param quantity Number of xp <em>earned</em>.
     */
    public static void addXp(int quantity) {
        while (quantity > 0) {
            int gap = XP_GAPS[level - 1 % XP_GAPS.length];

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

    public static void update(Event event, Map<String, Object> data) {
        if (!event.equals(Event.INFO_CHANGE))
            throw new Error("The UserData was listening for an unexptected event: " + event.toString());

        data.keySet().forEach(key -> {
            if (key.equals("nickname"))
                setNickname((String) data.get("nickname"));
            else if (key.equals("icon"))
                setIcon((String) data.get("icon"));
            else if (key.equals("reset"))
                reset();
        });

    }
}
