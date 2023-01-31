package model.players;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/* --- JUno ------------------------------- */

import model.cards.Card;

/**
 * This class contains those player info (user or AI) that are also used during
 * the game: nickanem, icon, cards in hand.
 */
public class Player {
    /* --- Fields ----------------------------- */

    private String icon;
    private String nickname;
    private List<Card> hand;

    /* --- Constructors ----------------------- */

    /**
     * Creates a new player with the given icon and nickname, and an empty hand.
     * 
     * @param icon     The icon of the player.
     * @param nickname The nickname of the player.
     */
    public Player(String icon, String nickname) {
        this.icon = icon;
        this.nickname = nickname;
        hand = new LinkedList<Card>();
    }

    /* --- Getters and Setters ---------------- */

    public String getNickame() {
        return nickname;
    }

    public String getIcon() {
        return icon;
    }

    public List<Card> getHand() {
        return hand;
    }

    /* --- Body ------------------------------- */

    /**
     * Wraps the player info and returns it.
     * 
     * @return The player data: "nickname" (String), "icon" (String) and "hand-size"
     *         (int).
     */
    public HashMap<String, Object> getData() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("nickname", getNickame());
        data.put("icon", getIcon());
        data.put("hand-size", getHand().size());
        return data;
    }
}
