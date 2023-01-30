package model.gameEntities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import model.data.UserData;
import model.gameObjects.Card;

/**
 * This class contains all the info about the player (user or AI) state.
 */
public class Player {
    /* --- Fields ----------------------------- */

    private String icon;
    private String nickname;
    private List<Card> hand;

    /* ---.--- Getters and Setters ------------ */

    /**
     * @return Player nickname.
     */
    public String getNickame() {
        return nickname;
    }

    /**
     * @return The path to the image used as player icon.
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @return The group of cards held by the player.
     */
    public List<Card> getHand() {
        return hand;
    }

    /* --- Constructors ----------------------- */

    /**
     * @param data The path to the file containing player's info.
     */
    public Player(String icon, String nickname) {
        this.icon = icon;
        this.nickname = nickname;
        hand = new LinkedList<Card>();
    }

    /**
     * Uses the data of the user.
     */
    public Player() {
        this.icon = UserData.getIcon();
        this.nickname = UserData.getNickname();
        hand = new LinkedList<Card>();
    }

    /* --- Body ------------------------------- */


    public HashMap<String, Object> getData() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("nickname", getNickame());
        data.put("icon", getIcon());
        data.put("hand-size", getHand().size());
        return data;
    }
}
