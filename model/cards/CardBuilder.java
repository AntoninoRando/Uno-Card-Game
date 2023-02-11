package model.cards;

import java.io.FileReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Loads all cards of a set from a JSON file and stores them so that they can be
 * easily fetched.
 */
public abstract class CardBuilder {
    /* --- Fields ----------------------------- */

    private static Card[] cards;

    /**
     * Gets the list of cards from the given set. If those cards have not been
     * already loaded, loads them.
     * 
     * @param setName The json file name.
     * @return The list of cards.
     */
    public static Card[] getCards(String setName) {
        if (cards == null)
            load(setName);
        return cards;
    }

    /* --- Body ------------------------------- */

    /**
     * Loads and stores all the cards contained in the json file.
     * 
     * @param setName The set name (i.e., the json file).
     */
    private static void load(String setName) {
        // parse the JSON file and save the result in the cards list
        List<Map<String, Object>> setCards = parseJson(setName);
        List<Card> cards = new LinkedList<Card>();

        Map<String, BiFunction<Suit, Integer, Card>> constructors = Map.of(
                "simple", (suit, value) -> new SimpleCard(suit, value),
                "draw 4", (suit, value) -> new DrawAndColor(suit, value, 4),
                "draw 2", (suit, value) -> new DrawCard(suit, value, 2),
                "reverse", (suit, value) -> new ReverseCard(suit, value),
                "block", (suit, value) -> new BlockCard(suit, value),
                "change color", (suit, value) -> new ChoseColor(suit, value));

        for (Map<String, Object> info : setCards) {
            Suit suit = Suit.valueOf((String) info.get("suit"));
            int value = (int) (long) info.get("value");
            int copies = (int) (long) info.get("copies");

            for (int i = 0; i < copies; i++) {
                Card card = constructors.get((String) info.get("type")).apply(suit, value);
                cards.add(card);
            }
        }

        CardBuilder.cards = cards.toArray(Card[]::new);
    }

    
    /** 
     * @param fileName
     * @return List<Map<String, Object>>
     */
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
