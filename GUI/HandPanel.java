package GUI;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;

import CardsTools.Card;
import CardsTools.Hand;

import GameTools.Controller;
import GameTools.GUnit;

public class HandPanel extends JPanel implements ActionListener {
    // VARIABLES
    private Controller controller;
    private Map<Card, JButton> cardButtons = new LinkedHashMap<Card, JButton>();
    private JButton drawButton;

    // CONSTRUCTORS
    public HandPanel(Controller controller) {
        this.controller = controller;
        controller.setCardListener(this::updateButtons);

        setLayout(new FlowLayout());

        drawButton = new JButton("draw");
        drawButton.addActionListener(this);
        add(drawButton);
        updateButtons();
    }

    // METHODS
    // !Questo metodo è poco efficente
    private void updateButtons() {
        for (Card card : getHand()) {
            if (cardButtons.containsKey(card)) 
                continue;
            
            JButton newButton = new JButton(card.toString());
            newButton.addActionListener(this); // Add the functionality

            cardButtons.put(card, newButton);
            add(newButton); // Adding to the layout
        }
        
        ArrayList<Card> buttonsToRemove = new ArrayList<Card>();
        for (Card card : cardButtons.keySet()) {
            if (!getHand().contains(card))
                buttonsToRemove.add(card);
        }
        for (Card card : buttonsToRemove) {
            JButton oldButton = cardButtons.remove(card);
            remove(oldButton); // Removing from the layout
        }

        revalidate(); // !Non so quale sia la funzione ma forse serve per applicare i cambiamenti.
        repaint();
    }


    // GETTERS AND SETTERS
    public Hand getHand() {
        return controller.getBringer().getHand();
    }

    // ActionListener METHODS
    @Override
    public void actionPerformed(ActionEvent e) {
        // !Per stare attenti a differenziare persino carte con stesso colore e seme, potrei usare l'hashCode.

        JButton premuto = (JButton) e.getSource();

        if (premuto == drawButton) {
            synchronized (controller) {
                GUnit.giveInput(-1);
                controller.notify();
            }
            return;
        }

        for (Map.Entry<Card, JButton>  entry: cardButtons.entrySet()) {
            if (premuto != entry.getValue()) // ! uso == e non equals perché deve essere lo stesso oggetto.
                continue;

            synchronized (controller) {
                GUnit.giveInput(controller, entry.getKey());
                controller.notify();
            }
        }
    }
}
