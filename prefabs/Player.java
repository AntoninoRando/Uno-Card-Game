package prefabs;

import java.util.TreeSet;

import model.data.PlayerData;
import model.gameLogic.Effect;

/**
 * This class contains all the info about the player state.
 */
public class Player {
    private String nick;
    private String icon;
    private boolean isHuman;
    private CardGroup hand;
    private TreeSet<Effect> conditions;

    public Player(String DataFilepath) {
        PlayerData pd = PlayerData.getPlayerData(DataFilepath);
        nick = pd.getNick();
        icon = pd.getIcon();
        isHuman = pd.isHuman();
        hand = new CardGroup();
        conditions = new TreeSet<>();
    }

    // Getters and Setters

    public String getNick() {
        return nick;
    }

    public String getIcon() {
        return icon;
    }

    public boolean isHuman() {
        return isHuman;
    }

    public CardGroup getHand() {
        return hand;
    }

    /* OTHERS */

    public void addCondition(Effect condition) {
        conditions.add(condition);
    }

    public void consumeConditions() {
        conditions.forEach(effect -> effect.cast(this, null));
        conditions.clear();
    }
}
