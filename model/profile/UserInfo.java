package model.profile;

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

    private static HashMap<Integer, Consumer<String>> ReadMap = fillReadMap();

    public static String getNick() {
        return nick;
    }

    public static void setNick(String nick) {
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

    private static HashMap<Integer, Consumer<String>> fillReadMap() {
        HashMap<Integer, Consumer<String>> map = new HashMap<>();

        map.put(0, line -> nick = line);
        map.put(1, line -> level = Integer.parseInt(line));
        map.put(2, line -> xp = Integer.parseInt(line));
        map.put(3, line -> games = Integer.parseInt(line));
        map.put(4, line -> winRate = Double.parseDouble(line));
        
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
        } catch (IOException e) {
        }
    }
}
