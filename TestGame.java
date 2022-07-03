import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import controller.Controller;
import controller.HumanController;
import model.MainLoop;
import model.Player;
import model.cards.Card;
import model.cards.Deck;
import model.cards.Suit;
import model.effects.EffectBuilder;

public class TestGame {
    public static void main(String[] args) {
        /* STANDARD DECK */
        /* ------------- */
        EffectBuilder draw2 = new EffectBuilder(3);
        draw2.directTargetToFollowing(1).addDraw(2).addBlockTurn();

        EffectBuilder block = new EffectBuilder(2);
        block.directTargetToFollowing(1).addBlockTurn();

        List<Card> standardSet = new ArrayList<Card>(108);
        for (Suit color : Suit.values()) {
            if (color == Suit.WILD) {
                continue;
            }
            for (int i = 1; i < 10; i++) {
                standardSet.add(new Card(color, i));
                standardSet.add(new Card(color, i));
            }
            standardSet.add(new Card(color, 0));
            standardSet.add(new Card(color, 0));
            
            Card d2a = new Card(color, -2);
            d2a.addEffect(draw2.build());
            standardSet.add(d2a);
            Card d2b = new Card(color, -2);
            d2b.addEffect(draw2.build());
            standardSet.add(d2b);

            Card ba = new Card(color, -1);
            ba.addEffect(block.build());
            standardSet.add(ba);
            Card bb = new Card(color, -1);
            bb.addEffect(block.build());
            standardSet.add(bb);
        }

        Deck standardDeck = new Deck(standardSet);

        // New players with their controller
        Player p1 = new Player("Antonino", true);
        Player p2 = new Player("Bot Giovanni", false);
        Player p3 = new Player("Bot Luca", false);

        Controller c1 = new HumanController();
        c1.setSource(p1);

        TreeMap<Integer, Player> players = new TreeMap<>();
        players.put(0, p1);
        players.put(1, p2);
        players.put(2, p3);

        MainLoop.getInstance().play(players, standardDeck, c1);
    }
}
