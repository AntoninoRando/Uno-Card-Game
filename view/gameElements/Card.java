package view.gameElements;

import java.util.HashMap;

import javafx.scene.image.ImageView;
import view.Sprite;
import view.SpriteFactory;
import view.media.Animations;
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
        makeZommable();
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

    private void makeZommable() {
        setOnMouseEntered(__ -> Animations.zoomIn(this, 0.5));
        setOnMouseExited(__ -> Animations.zoomOut(this, 0.5));
    }
}
