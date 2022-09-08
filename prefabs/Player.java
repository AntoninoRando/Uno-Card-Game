package prefabs;

import java.util.TreeSet;

import model.data.PlayerData;
import model.gameLogic.Effect;

/**
 * This class contains all the info about the player state.
 */
public class Player {
    private PlayerData playerData;
    private CardGroup hand;
    private TreeSet<Effect> conditions;

    public Player(String DataFilepath) {
        playerData = PlayerData.getPlayerData(DataFilepath);
        hand = new CardGroup();
        conditions = new TreeSet<>();
    }

    public Player() {
        playerData = PlayerData.getThisForStaticCalls();
        hand = new CardGroup();
        conditions = new TreeSet<>();
    }

    // Getters and Setters

    public PlayerData info() {
        return playerData;
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
