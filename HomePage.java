import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class HomePage extends JFrame {

    private JTextArea textArea;
    private JTextField textField;
    private JButton button;

    // CONSTRUCTORS
    public HomePage() {
        // Chiamiamo questa finestra "Home page"
        super("Home page");

        // Il LayoutManager serve a posizionare e ridimensionare correttamente gli elementi all'interno della finestra
        setLayout(new BorderLayout());

        // Creiamo i componenti all'interno del frame...
        textArea = new JTextArea();
        textField = new JTextField();
        button = new JButton("Draw card"); // Testo da visualizzare nel bottone
        // ...e aggiungiamoli (nelle posizioni disponibili nel layout usato)
        add(textArea, BorderLayout.CENTER);
        add(textField, BorderLayout.PAGE_START);
        add(button, BorderLayout.PAGE_END);

        // Facciamo eseguire un'azione al nostro bottone.
        // ActionListener è un'interffacia funzionale, quindi possiamo utilizzare le classi anonime
        button.addActionListener(new ActionListener() {
            @Override // Alla pressione del bottone...
            public void actionPerformed(ActionEvent e) {
                // ...aggiungiamo del testo alla nostra textArea
                textArea.append("New Card drew\n");
            }
        });

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
