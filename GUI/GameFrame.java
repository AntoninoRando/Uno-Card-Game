package GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;

import CardsTools.Card;

import javax.swing.JButton;

import GameTools.GameController;

public class GameFrame extends JFrame {
    private HandPanel handPanel;
    private GameController game;
    private JButton terrainCard;

    // CONSTRUCTORS
    public GameFrame(GameController currentGame) {
        // Chiamiamo questa finestra "Home page"
        super("");

        // Iniziamo una partita
        game = currentGame;
        setupFrame();

        // Impostiamo la dimensione della finestra in pixels e decidiamo che non può
        // essere ridimensionata
        setSize(800, 500);
        setResizable(true);

        // Facciamo apparire la finestra al centro dello schermo
        setLocationRelativeTo(null);

        // Impostiamo che quando chiudiamo la finestra finisce anche il programma
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Rendiamo la finestra visibile. E' bene farlo alla fine perche' prima la
        // finestra deve essere impostata
        setVisible(true);

        game.playWithNoSetup();
    }

    // METHODS
    private void setupFrame() {
        game.setup();
        game.setCardListener(this::updateCurrentCard);
        game.setGameWonListener(this::resetPage);

        // Il LayoutManager serve a posizionare e ridimensionare correttamente gli
        // elementi all'interno della finestra
        setLayout(new BorderLayout());

        // Creiamo i componenti all'interno del frame...
        handPanel = new HandPanel(game.getControllers()[0]);
        terrainCard = new JButton(game.getGame().getCurrentCard().toString());
        // ...e aggiungiamoli (nelle posizioni disponibili nel layout usato)
        add(handPanel, BorderLayout.PAGE_END);
        add(terrainCard, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private void updateCurrentCard() {
        Card card = game.getGame().getCurrentCard();
        terrainCard.setText(card.toString());
    }

    private void resetPage(String winnerNick) {
        getContentPane().removeAll();

        JLabel winLabel = new JLabel("Well done " + winnerNick + ", you won!", JLabel.CENTER);
        add(winLabel);

        JButton playAgaiButton = new JButton("Play again");
        playAgaiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll();
                setupFrame();

                // game.playWithNoSetup(); va fatto con il multithreading altrimenti non
                // risetuppa finché non finisce questo metodo. Infatti sotto inizio un nuovo
                // thread ma non so quanto sia efficiente.
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        game.playWithNoSetup();
                    }

                }).start();
            }
        });
        add(playAgaiButton, BorderLayout.PAGE_START);

        revalidate();
        repaint();
    }
}
