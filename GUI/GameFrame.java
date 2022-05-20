package GUI;
import java.awt.BorderLayout;


import javax.swing.JFrame;

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
        game.setup();

        // Il LayoutManager serve a posizionare e ridimensionare correttamente gli elementi all'interno della finestra
        setLayout(new BorderLayout());

        // Creiamo i componenti all'interno del frame...
        handPanel = new HandPanel(game.getControllers()[0].getBringer().getHand());
        terrainCard = new JButton(game.getGame().getCurrentCard().toString());
        // ...e aggiungiamoli (nelle posizioni disponibili nel layout usato)
        add(handPanel, BorderLayout.PAGE_END);
        add(terrainCard, BorderLayout.CENTER);

        // Impostiamo la dimensione della finestra in pixels e decidiamo che non può essere ridimensionata
        setSize(800, 500);
        setResizable(false);

        // Facciamo apparire la finestra al centro dello schermo
        setLocationRelativeTo(null);

        // Impostiamo che quando chiudiamo la finestra finisce anche il programma
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Rendiamo la finestra visibile. E' bene farlo alla fine perche' prima la finestra deve essere impostata
        setVisible(true);
    }

    // private void setTerrainCard() {
    //     Card card = game.getGame().getCurrentCard();
    // }
}
