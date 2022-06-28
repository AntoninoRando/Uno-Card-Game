package GUI;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.Image;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicOptionPaneUI.ButtonActionListener;

import CardsTools.Card;
import CardsTools.Hand;
import model.Controller;
import model.GUnit;

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
            
            try {
                // !Tutto quello che ho fatto sull'immagine l'ho preso da internet.
                BufferedImage buttonIcon = ImageIO.read(new File("../../AllUnoCards/"+card+".png"));
                ImageIcon buttonImage = new ImageIcon(buttonIcon);

                Image image = buttonImage.getImage();
                // !La proporzione originale era 216x336 px (il file vero e proprio era ancora più grande)
                Image newImage = image.getScaledInstance(100, 155, java.awt.Image.SCALE_SMOOTH);

                JButton newButton = new JButton(new ImageIcon(newImage));
                newButton.addActionListener(this); // Add the functionality

                // !Leviamo i particolari del bottone per lasciare solo l'immagine.
                newButton.setBorder(BorderFactory.createEmptyBorder());
                newButton.setContentAreaFilled(false);

                cardButtons.put(card, newButton);
                add(newButton); // Adding to the layout
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
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
