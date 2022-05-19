package GUI;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import CardsTools.Card;
import CardsTools.Hand;

public class HandPanel extends JPanel {
    // VARIABLES
    private Hand cards;

    // CONSTRUCTORS
    public HandPanel(Hand cards) {
        this.cards = cards;
        setLayout(new FlowLayout());

        for (Card card : this.cards.getCards()) {
            JButton cardButton = new JButton(card.toString());
            add(cardButton);
        }
    }
}
