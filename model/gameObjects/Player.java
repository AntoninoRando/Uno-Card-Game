package model.gameObjects;

import java.util.HashMap;
import java.util.TreeSet;

import model.data.PlayerData;
import model.gameLogic.Effect;

/**
 * This class contains all the info about the player (user or AI) state.
 */
public class Player {
    /* --- Fields ----------------------------- */

    private String nick;
    private String icon;
    private boolean isHuman;
    private CardGroup hand;
    private TreeSet<Effect> conditions;

    /**
     * @return Player nickname.
     */
    public String getNick() {
        return nick;
    }

    /**
     * @return The path to the image used as player icon.
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @return Whether the player is the user or the AI.
     */
    public boolean isHuman() {
        return isHuman;
    }

    /**
     * @return The group of cards held by the player.
     */
    public CardGroup getHand() {
        return hand;
    }

    /* --- Constructors ----------------------- */

    /**
     * @param data The path to the file containing player's info.
     */
    public Player(String data) {
        PlayerData pd = PlayerData.getPlayerData(data);
        nick = pd.getNick();
        icon = pd.getIcon();
        isHuman = pd.isHuman();
        hand = new CardGroup();
        conditions = new TreeSet<>();
    }

    /* --- Body ------------------------------- */

    public void addCondition(Effect condition) {
        conditions.add(condition);
    }

    public void consumeConditions() {
        conditions.forEach(effect -> effect.cast(this, null));
        conditions.clear();
    }

    public HashMap<String, Object> getData() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("nickname", getNick());
        data.put("icon", getIcon());
        data.put("hand-size", getHand().size());
        return data;
    }
}
