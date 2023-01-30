package view.gameElements;

import java.util.HashMap;

import javafx.animation.ScaleTransition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import view.Sprite;
import view.SpriteFactory;
import events.EventListener;

/**
 * Implements the <em>Flyweight</em> pattern as <b>Context</b>.
 */

/**
 * The GUI representation of a UNO card: it has an image and it is zoombale, but
 * it does not store card informations.
 */
public class Card extends ImageView implements EventListener {
    /* --- Fields ----------------------------- */

    private final ScaleTransition zoomIn = new ScaleTransition(Duration.millis(100.0), this);
    private final ScaleTransition zoomOut = new ScaleTransition(Duration.millis(100.0), this);

    public static HashMap<Integer, Card> cards = new HashMap<>();

    private Sprite cardSprite; // flyweight (i.e., repeating state)

    /* --- Constructors ----------------------- */

    /**
     * Create a UNO card with the image associated to the given input card.
     * 
     * @param card The card info; used to detect which image to load, not to stores
     *             information about the card.
     */
    public Card(int tag, String cardString) {
        getStyleClass().add("card");
        makeZommable(0.5);

        context(tag, cardString);
        draw();
    }

    /* --- Body ------------------------------- */

    /**
     * 
     * @param tag        (unique state)
     * @param cardString (repeating state)
     */
    public void context(int tag, String cardString) {
        cardSprite = SpriteFactory.getCardSprite(cardString);
        cards.put(tag, this);
    }

    // operation
    public void draw() {
        cardSprite.draw(150.0, this);
    }

    // TODO migliorare perche' e' un po' lagghi e ogni tanto si bugga
    private void makeZommable(Double scalingFactor) {
        setOnMouseEntered(e -> {
            if (getScaleX() <= 1.0 || getScaleY() <= 1.0) {
                // 1 + scalingFactor = getScaleX() + x
                zoomIn.setByX(1 + scalingFactor - getScaleX());
                zoomIn.setByY(1 + scalingFactor - getScaleY());
                zoomIn.play();
            }
        });

        setOnMouseExited(e -> {
            if (getScaleX() != 1.0 || getScaleY() != 1.0) {
                // 1 = getScaleX() - x
                zoomOut.setByX(1 - getScaleX());
                zoomOut.setByY(1 - getScaleY());
                zoomOut.play();
            }
        });
    }
}
