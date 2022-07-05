import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import controller.Controller;
import controller.HumanController;
import model.Game;
import model.MainLoop;
import model.Player;
import model.cards.Card;
import model.cards.Deck;
import model.cards.Suit;
import model.effects.EffectBuilder;
import model.events.EventListener;

public class TestGame {
    public static void main(String[] args) {
        /* STANDARD DECK */
        /* ------------- */
        EffectBuilder cc = new EffectBuilder(4);
        cc.askForInput("Pick a new color: \"red\", \"blue\", \"green\", \"yellow\":\n")
                .addChangePlayCondition((__) -> false) // Qualsiasi carta giocata prima della scelta del giocatore non
                                                       // sarà valida.
                .addKeepTurn()
                .addEvent("InputGiven", new EventListener() {
                    @Override
                    public void update(String eventType, Object data) {
                        Suit con;
                        switch ((String) data) {
                            case "red":
                                con = Suit.RED;
                                break;
                            case "blue":
                                con = Suit.BLUE;
                                break;
                            case "green":
                                con = Suit.GREEN;
                                break;
                            case "yellow":
                                con = Suit.YELLOW;
                                break;
                            default:
                                con = Suit.WILD;
                                break;
                        }
                        Game.getInstance().setPlayConditon((card) -> card.getSuit() == con);
                        // We remove this functionality after this choice.
                        MainLoop.getInstance().events.unsubscribe("InputGiven", this);
                        // After playing the next card, reset the old condition.
                        MainLoop.getInstance().events.subscribe("CardChanged", (t_, d_) -> {
                            Game.getInstance().setPlayConditonToDefault();
                        });
                        // Passa il turno al giocatore successivo
                        MainLoop.getInstance().playTurn(Game.getInstance().getTurn());
                        MainLoop.getInstance().enemiesTurn();
                    }
                });

        EffectBuilder draw2 = new EffectBuilder(3);
        draw2.directTargetToFollowing(1).addDraw(2).addBlockTurn();

        EffectBuilder block = new EffectBuilder(2);
        block.directTargetToFollowing(1).addBlockTurn();

        List<Card> standardSet = new ArrayList<Card>(108);
        for (Suit color : Suit.values()) {
            if (color == Suit.WILD) {
                for (int i = 1; i < 9; i++) {
                    Card changeColor = new Card(color, -5);
                    changeColor.addEffect(cc.build());
                    standardSet.add(changeColor);
                }
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

        /* LOOP */
        /* ---- */
        MainLoop.getInstance().play(players, standardDeck, c1);

        // String[] toListen = new String[] {"cardPlayed", "playerDrew", "turnStart", "invalidChoice", "warning", "turnEnd"};
        // try {
        //     Loop.getInstance().play(ConsoleOutput.getInstance(), toListen, players, standardDeck, c1);
        // } catch (InterruptedException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
    }
}
