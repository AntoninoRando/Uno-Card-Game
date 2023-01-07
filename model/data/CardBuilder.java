package model.data;

import java.io.FileReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import events.EventType;
import model.CUModel;

/* --- Mine ------------------------------- */

import model.gameObjects.*;

/**
 * Loads all cards of a set through the <code>load</code> method. Those cards
 * are grouped by their set-name in the <code>allCards</code> field.
 */
public abstract class CardBuilder {
    /* --- Fields ----------------------------- */

    private static CardGroup cards;

    public static CardGroup getCards(String setName) {
        if (cards == null)
            load(setName);
        return cards;
    }

    /* --- Body ------------------------------- */

    public static void load(String setName) {
        // parse the JSON file and save the result in the cards list
        List<Map<String, Object>> setCards = parseJson(setName);
        CardBuilder.cards = new CardGroup();

        for (Map<String, Object> info : setCards) {
            Suit suit = Suit.valueOf((String) info.get("suit"));
            int value = (int) (long) info.get("value");
            int copies = (int) (long) info.get("copies");

            Map<String, Supplier<Card>> cardConstructor = Map.of(
                "simple", () -> new SimpleCard(suit, value),
                "draw 4", () -> new DrawCard(suit, value, 4),
                "draw 2", () -> new DrawCard(suit, value, 2),
                "reverse", () -> new ReverseCard(suit, value),
                "block", () -> new BlockCard(suit, value)
            );

            for (int i = 0; i < copies; i++) {
                Card card = cardConstructor.get((String) info.get("type")).get();
                cards.add(card);
                CUModel.communicate(EventType.NEW_CARD, card.getData());
            }
        }
    }

    private static List<Map<String, Object>> parseJson(String fileName) {
        // create a new JSON parser
        JSONParser parser = new JSONParser();

        // create a new list to store the cards
        List<Map<String, Object>> cards = new ArrayList<>();

        try {
            // parse the JSON file and store the result in the JSONArray cardsJson
            JSONArray cardsJson = (JSONArray) parser.parse(new FileReader(fileName));

            // iterate over the cardsJson array and add each element to the cards list
            for (Object cardJson : cardsJson) {
                // convert the cardJson object to a JSONObject
                JSONObject card = (JSONObject) cardJson;

                // create a new HashMap to store the card data
                Map<String, Object> cardData = new HashMap<>();

                // iterate over the card and add each key-value pair to the cardData map
                for (Object key : card.keySet()) {
                    String keyString = (String) key;
                    Object value = card.get(keyString);
                    cardData.put(keyString, value);
                }

                // add the cardData map to the cards list
                cards.add(cardData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // return the cards list
        return cards;
    }
}
