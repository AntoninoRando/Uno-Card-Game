package model.data;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import model.gameLogic.EffectBuilder;
import prefabs.Card;
import prefabs.CardGroup;
import prefabs.Suit;

public abstract class CardsInfo {
    public static HashMap<String, Card> allCards = new HashMap<>();

    public static CardGroup load(String setName) {
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser()
                    .parse(new FileReader("resources/Cards/" + setName + ".json"));
            List<Card> standardSet = new ArrayList<Card>(jsonObject.keySet().size());
            for (Object cardKey : jsonObject.keySet()) {
                Object cardValues = jsonObject.get(cardKey);

                Suit suit = Suit.valueOf((String) ((JSONObject) cardValues).get("Suit"));

                int value = (int) (long) ((JSONObject) cardValues).get("Value");

                Long quantity = (Long) ((JSONObject) cardValues).get("Quantity");
                if (quantity == null)
                    quantity = 1L;

                JSONArray effect = (JSONArray) (((JSONObject) cardValues).get("Effect"));

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
                for (int i = 0; i < quantity; i++)
                    standardSet.add(card.getCopy());
            }
            return new CardGroup(standardSet);
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } catch (ParseException e) {
        }
        return null;
    }
}
