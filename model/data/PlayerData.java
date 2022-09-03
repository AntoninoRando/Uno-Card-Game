package model.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;

import events.ActionListener;

/**
 * A class containig player's info. It also contains static info about the user.
 */
public class PlayerData implements ActionListener {
    // Used by bots and user
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

    /**
     * 
     * @param filePathname The path of the file containing the player's info.
     */
    private PlayerData(String filePathname) {
        loadData(filePathname);
        if (isHuman)
            Runtime.getRuntime().addShutdownHook(new Thread(() -> writeData(filePathname)));
    }

    private PlayerData() {
    }

    public static PlayerData getPlayerData(String filePathname) {
        return dataLoaded.containsKey(filePathname) ? dataLoaded.get(filePathname) : new PlayerData(filePathname);
    }

    public static PlayerData getThisForStaticCalls() {
        return new PlayerData();
    }

    // GETTERS AND SETTERS

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
    private static void setNick(String nick) {
        nick = nick.trim();
        if (!nick.equals("") && nick.length() <= Info.getNickMaxLength())
            PlayerData.userNick = nick;
        Info.events.notify("newUserNick", nick);
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
    private static void setIcon(String icon) {
        PlayerData.userIcon = icon;
        Info.events.notify("newUserIcon", icon);
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
    public static double getWins() {
        return wins;
    }

    //

    /**
     * Add the <code>quantity</code> number of xp to the <b>user</b>.
     * 
     * @param quantity Number of xp earned.
     */
    public static void addXp(int quantity) {
        int gap = Info.getXpGap(level);

        if (xp + quantity < gap) {
            PlayerData.xp += quantity;
            Info.events.notify("xpEarned", PlayerData.xp, PlayerData.level, Info.userLevelProgress());
            return;
        }

        quantity -= gap - xp;
        PlayerData.xp = 0;
        PlayerData.level++;
        addXp(quantity);
    }

    /**
     * Mark another game played by the <b>user</b>. If <code>win</code> is true,
     * mark also another game won by the user.
     * 
     * @param win If the user won the game played.
     */
    public static void addGamePlayed(boolean win) {
        PlayerData.games++;
        if (win)
            PlayerData.wins++;
        Info.events.notify("gamePlayed", PlayerData.games, PlayerData.wins);
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

        Info.events.notify("infoResetted", PlayerData.getUserNick(), PlayerData.getUserIcon());
    }

    //

    private HashMap<Integer, Consumer<String>> ReadMap = fillReadMap();

    private void loadData(String filePathname) {
        try (FileInputStream fis = new FileInputStream(filePathname)) {
            Scanner sc = new Scanner(fis);

            int i = 0;
            while (sc.hasNextLine()) {
                ReadMap.get(i).accept(sc.nextLine());
                i++;
            }
            sc.close();
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
        map.put(2, line -> PlayerData.level = Integer.parseInt(line));
        map.put(3, line -> PlayerData.xp = Integer.parseInt(line));
        map.put(4, line -> PlayerData.games = Integer.parseInt(line));
        map.put(5, line -> PlayerData.wins = Integer.parseInt(line));
        map.put(6, line -> {
            if (isHuman())
                PlayerData.userIcon = line;
            else
                icon = line;
        });

        return map;
    }

    private void writeData(String filePathname) {
        try (FileOutputStream fos = new FileOutputStream(filePathname)) {
            fos.write((isHuman ? "true" : "false").getBytes());
            fos.write("\n".getBytes());
            fos.write(getNick().getBytes());
            fos.write("\n".getBytes());
            fos.write(Integer.toString(level).getBytes());
            fos.write("\n".getBytes());
            fos.write(Integer.toString(xp).getBytes());
            fos.write("\n".getBytes());
            fos.write(Integer.toString(games).getBytes());
            fos.write("\n".getBytes());
            fos.write(Integer.toString(wins).getBytes());
            fos.write("\n".getBytes());
            fos.write(getIcon().getBytes());
        } catch (IOException e) {
        }
    }

    @Override
    public void requestChange(String type, String data) {
        switch (type) {
            case "nick":
                PlayerData.setNick(data);
                break;
            case "icon":
                PlayerData.setIcon(data);
                break;
        }
    }
}
