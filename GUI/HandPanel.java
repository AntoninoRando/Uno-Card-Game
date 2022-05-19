package GUI;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import CardsTools.Card;
import CardsTools.Hand;
import Controllers.Controller;
import CardsTools.CardGroup;

public class HandPanel extends JPanel implements ActionListener {
    // VARIABLES
    private Controller controller;
    private Hand cards = new Hand();
    private List<JButton> cardButtons = new LinkedList<JButton>();

    // CONSTRUCTORS
    public HandPanel(Hand firstHand) {
        setLayout(new FlowLayout());
        addCards(firstHand);
    }

    protected boolean addCard(Card card) {
        boolean isAdded = cards.add(card);
        if (isAdded) {
            JButton cardButton = new JButton(card.toString());
            cardButton.addActionListener(this);

            cardButtons.add(cardButton);
            add(cardButton);
        }
        return isAdded;
    }

    protected boolean addCards(CardGroup cards) {
        boolean allAdded = true;
        for (Card card : cards.getCards()) {
            allAdded = allAdded && addCard(card); // !Non so se esista l'operatore &=
        }
        return allAdded;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Prova");
    }
}
