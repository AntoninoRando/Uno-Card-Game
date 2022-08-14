package model.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;

public abstract class UserInfo {
    private static String nick;
    private static int level;
    private static int xp;
    private static int games;
    private static double winRate;
    private static HashMap<Integer, Integer> xpGaps = fillXpGaps();
    private static String iconPath;

    private static int nickMaxLenght = 22;

    private static HashMap<Integer, Consumer<String>> ReadMap = fillReadMap();

    public static String getNick() {
        return nick;
    }

    public static void setNick(String nick) {
        nick = nick.trim();
        if (!nick.equals("") && nick.length() <= nickMaxLenght)
            UserInfo.nick = nick;
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

    public static double getWinRate() {
        return winRate;
    }

    public static int getXpGap(int level) {
        return xpGaps.get((level - 1) % xpGaps.size());
    }

    public static int getXpGap() {
        return getXpGap(level);
    }

    public static String getIconPath() {
        return iconPath;
    }

    public static void setIconPath(String iconPath) {
        UserInfo.iconPath = iconPath;
    }

    public static void addXp(int quantity) {
        if (UserInfo.xp + quantity > getXpGap()) {
            quantity -= getXpGap() - UserInfo.xp;
            UserInfo.xp = 0;
            UserInfo.level++;
            addXp(quantity);
        } else
            UserInfo.xp += quantity;
    }

    public static void addGamePlayed(boolean win) {
        games++;
        if (win)
            winRate++;
    }

    public static void reset() {
        UserInfo.nick = "User";
        UserInfo.level = 1;
        UserInfo.xp = 0;
        UserInfo.games = 0;
        UserInfo.winRate = 0;
        UserInfo.iconPath = "resources\\icons\\night.png";
    }

    private static HashMap<Integer, Consumer<String>> fillReadMap() {
        HashMap<Integer, Consumer<String>> map = new HashMap<>();

        map.put(0, line -> nick = line);
        map.put(1, line -> level = Integer.parseInt(line));
        map.put(2, line -> xp = Integer.parseInt(line));
        map.put(3, line -> games = Integer.parseInt(line));
        map.put(4, line -> winRate = Double.parseDouble(line));
        map.put(5, line -> iconPath = line);
        
        return map;
    }

    private static HashMap<Integer, Integer> fillXpGaps() {
        HashMap<Integer, Integer> map = new HashMap<>();

        map.put(0, 5);
        map.put(1, 8);
        map.put(2, 15);
        map.put(3, 21);
        map.put(4, 24);
        map.put(5, 28);
        map.put(6, 31);
        map.put(7, 35);
        map.put(8, 39);
        map.put(9, 50);
        
        return map;
    }

    public static void loadData(String filePathname) {
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
    }

    public static void writeData(String filePathname) { 
        try (FileOutputStream fos = new FileOutputStream(filePathname)) {
            fos.write(nick.getBytes());
            fos.write("\n".getBytes());
            fos.write(Integer.toString(level).getBytes());
            fos.write("\n".getBytes());
            fos.write(Integer.toString(xp).getBytes());
            fos.write("\n".getBytes());
            fos.write(Integer.toString(games).getBytes());
            fos.write("\n".getBytes());
            fos.write(Double.toString(winRate).getBytes());
            fos.write("\n".getBytes());
            fos.write(iconPath.getBytes());
        } catch (IOException e) {
        }
    }
}