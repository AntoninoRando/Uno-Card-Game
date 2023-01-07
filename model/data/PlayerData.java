package model.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import events.Event;

/**
 * A class containig player's info. It also contains static info about the user.
 */
public class PlayerData {
    /* --- Fields ----------------------------- */
    
    // Used by both AI and user
    private String nick;
    private String icon;
    private boolean isHuman;
    // Used by user
    private static String userNick;
    private static String userIcon;
    private static int level;
    private static int xp;
    private static int games;
    private static int wins;
    // Data already Loaded
    private static HashMap<String, PlayerData> dataLoaded = new HashMap<>();
    
    private HashMap<Integer, Consumer<String>> ReadMap = fillReadMap();

    /* ---.--- Getters and Setters ------------ */

    /**
     * Get the <code>PlayerData</code> object associated with the given info file path.
     * @param filePathname The path of the file containing the player's info.
     * @return The <code>PlayerData</code> containing the info about the player.
     */
    public static PlayerData getPlayerData(String filePathname) {
        return dataLoaded.containsKey(filePathname) ? dataLoaded.get(filePathname) : new PlayerData(filePathname);
    }

    /**
     * 
     * @return The nickname of the player.
     */
    public String getNick() {
        return isHuman ? getUserNick() : nick;
    }

    /**
     * 
     * @return The nickname of the <b>user</b>.
     */
    public static String getUserNick() {
        return PlayerData.userNick;
    }

    /**
     * 
     * @param nick The new nickname of the <b>user</b>.
     */
    public static void setUserNick(String nick) {
        nick = nick.trim();
        if (!nick.equals("") && nick.length() <= Info.getNickMaxLength())
            PlayerData.userNick = nick;
        // Info.events.notify(EventType.USER_NEW_NICK, nick);
    }

    /**
     * 
     * @return The name of the icon file.
     */
    public String getIcon() {
        return isHuman ? getUserIcon() : icon;
    }

    /**
     * 
     * @return The icon of the <b>user</b>.
     */
    public static String getUserIcon() {
        return PlayerData.userIcon;
    }

    /**
     * 
     * @param icon The name of the new icon of the <b>user</b>.
     */
    public static void setUserIcon(String icon) {
        PlayerData.userIcon = icon;
        // Info.events.notify(EventType.USER_NEW_ICON, icon);
    }

    /**
     * 
     * @return If the player is the user (true) or a bot (false).
     */
    public boolean isHuman() {
        return isHuman;
    }

    /**
     * 
     * @return The level of the <b>user</b>.
     */
    public static int getLevel() {
        return level;
    }

    /**
     * 
     * @return The current xp of the <b>user</b>.
     */
    public static int getXp() {
        return xp;
    }

    /**
     * 
     * @return Number of games played by the <b>user</b>.
     */
    public static int getGames() {
        return games;
    }

    /**
     * 
     * @return Number of games won by the <b>user</b>.
     */
    public static int getWins() {
        return wins;
    }

    /* --- Constructors ----------------------- */

    private PlayerData(String filePathname) {
        loadData(filePathname);
        if (isHuman)
            Runtime.getRuntime().addShutdownHook(new Thread(() -> writeData(filePathname)));
    }

    private PlayerData() {
    }

    /* --- Body ------------------------------- */

    /**
     * Add the <code>quantity</code> number of xp to the <b>user</b>.
     * 
     * @param quantity Number of xp earned.
     */
    public static void addXp(int quantity) {
        int gap = Info.getXpGap(getLevel());
        int startingLevel =  getLevel();
        
        while (getXp() + quantity >= gap) {
            PlayerData.level++;
            gap += Info.getXpGap(level);
        }

        PlayerData.xp = Info.getXpGap(getLevel()) - (gap - quantity - getXp());
        // Info.events.notify(EventType.NEW_LEVEL_PROGRESS, Info.userLevelProgress());
        // if (PlayerData.getLevel() != startingLevel)
            // Info.events.notify(EventType.LEVELED_UP, PlayerData.level);
    }

    /**
     * Mark another game played by the <b>user</b>. If <code>win</code> is true,
     * mark also another game won by the user.
     * 
     * @param win If the user won the game played.
     */
    public static void addGamePlayed(boolean win) {
        PlayerData.games++;
        // Info.events.notify(EventType.USER_PLAYED_GAME, PlayerData.games);
        if (win) {
            PlayerData.wins++;
            // Info.events.notify(EventType.USER_WON, PlayerData.wins);
        }
    }

    /**
     * Reset all the info of the <b>user</b>.
     */
    public static void reset() {
        PlayerData.userNick = Info.getDefaultNick();
        PlayerData.userIcon = Info.getDefaultIcon();
        PlayerData.level = 0;
        PlayerData.xp = 0;
        PlayerData.games = 0;
        PlayerData.wins = 0;
        // Notify
        HashMap<String, Object> data = new HashMap<>();
        data.put("nickname", PlayerData.userNick);
        data.put("icon", PlayerData.userIcon);
        Info.events.notify(Event.INFO_RESET, data);
    }

    private void loadData(String filePathname) {
        try (Scanner sc = new Scanner(new FileInputStream(filePathname))) {
            int i = 0;
            while (sc.hasNextLine()) {
                ReadMap.get(i).accept(sc.nextLine());
                i++;
            }
        } catch (IOException e) {
        }

        dataLoaded.put(filePathname, this);
    }

    private HashMap<Integer, Consumer<String>> fillReadMap() {
        HashMap<Integer, Consumer<String>> map = new HashMap<>();

        map.put(0, line -> isHuman = Boolean.parseBoolean(line));
        map.put(1, line -> {
            if (isHuman())
                PlayerData.userNick = line;
            else
                nick = line;
        });
        map.put(2, line -> {
            if (isHuman())
                PlayerData.userIcon = line;
            else
                icon = line;
        });
        map.put(3, line -> PlayerData.level = Integer.parseInt(line));
        map.put(4, line -> PlayerData.xp = Integer.parseInt(line));
        map.put(5, line -> PlayerData.games = Integer.parseInt(line));
        map.put(6, line -> PlayerData.wins = Integer.parseInt(line));

        return map;
    }

    private void writeData(String filePathname) {
        try (FileOutputStream fos = new FileOutputStream(filePathname)) {
            StringJoiner sj = new StringJoiner("\n");
            sj.add("true").add(getUserNick()).add(getUserIcon());
            IntStream.of(level, xp, games, wins).mapToObj(x -> Integer.toString(x)).forEachOrdered(s -> sj.add(s));
            fos.write(sj.toString().getBytes());
        } catch (IOException e) {
        }
    }
}
