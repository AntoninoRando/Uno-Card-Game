package GUI;
import java.awt.BorderLayout;

import javax.swing.JFrame;

import GameTools.GameManager;

public class GameFrame extends JFrame {
    private HandPanel handPanel;
    private GameManager game;

    // CONSTRUCTORS
    public GameFrame(GameManager currentGame) {
        // Chiamiamo questa finestra "Home page"
        super("");

        // Iniziamo una partita
        game = currentGame;
        game.setup();

        // Il LayoutManager serve a posizionare e ridimensionare correttamente gli elementi all'interno della finestra
        setLayout(new BorderLayout());

        // Creiamo i componenti all'interno del frame...
        handPanel = new HandPanel(this.game.getControllers().get(0).getBringer().getHand());
        // ...e aggiungiamoli (nelle posizioni disponibili nel layout usato)
        add(handPanel, BorderLayout.PAGE_END);

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
}
