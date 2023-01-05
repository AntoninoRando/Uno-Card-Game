package model.data;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import events.EventType;

/* --- Mine ------------------------------- */

import model.gameLogic.EffectBuilder;
import model.gameLogic.Loop;
import model.gameObjects.*;

/**
 * Loads all cards of a set through the <code>load</code> method. Those cards
 * are grouped by their set-name in the <code>allCards</code> field.
 */
public abstract class CardsInfo {
    /* --- Fields ----------------------------- */

    public static HashMap<String, Card> allCards = new HashMap<>();

    /* --- Body ------------------------------- */

    public static CardGroup load(String setName) throws IOException, ParseException {
        JSONObject jsonObject = (JSONObject) new JSONParser().parse(new FileReader(Info.getCardsPath(setName)));
        List<Card> standardSet = new ArrayList<Card>(jsonObject.keySet().size());
        for (Object cardKey : jsonObject.keySet()) {
            Object cardValues = jsonObject.get(cardKey);

            Suit suit = Suit.valueOf((String) ((JSONObject) cardValues).get("Suit"));
            int value = (int) (long) ((JSONObject) cardValues).get("Value");
            Long quantity = (Long) ((JSONObject) cardValues).get("Quantity");
            JSONArray effect = (JSONArray) (((JSONObject) cardValues).get("Effect"));

            if (quantity == null)
                quantity = 1L;

            Card card;

            if (effect == null)
                card = new Card(suit, value);
            else {
                String[] efList = new String[effect.size()];
                for (int i = 0; i < effect.size(); i++)
                    efList[i] = (String) effect.get(i);
                card = new Card(suit, value, new EffectBuilder().build(efList));
            }

            allCards.put(setName + " " + (String) cardKey, card);
            for (int i = 0; i < quantity; i++) {
                Card copy = card.getCopy();
                standardSet.add(copy);
                Loop.events.notify(EventType.NEW_CARD, copy.getData());
            }
        }
        return new CardGroup(standardSet);

    }
}
