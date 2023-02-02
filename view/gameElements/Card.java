package view.gameElements;

import java.util.HashMap;

import javafx.scene.image.ImageView;

/* --- JUno ------------------------------- */

import view.Sprite;
import view.SpriteFactory;
import view.media.Animations;

/**
 * Implements the <em>Flyweight</em> pattern.
 * <p>
 * The GUI representation of a UNO card: it has an image and it is zoombale, but
 * it does not store card informations.
 */
public class Card extends ImageView {
    /* --- Fields ----------------------------- */

    public static HashMap<Integer, Card> cards = new HashMap<>();
    /**
     * The flyweight (i.e., the repeating state of the card).
     */
    private Sprite cardSprite;

    /* --- Constructors ----------------------- */

    /**
     * Creates a new image to see the card-string representation and applies it some
     * behaviors.
     * 
     * @param tag        The tag of the card, used to associate the image to the
     *                   actual card.
     * @param cardString The card representation as string, used to load the right
     *                   image.
     */
    public Card(int tag, String cardString) {
        getStyleClass().add("card");
        makeZommable();
        context(tag, cardString);
        draw();
    }

    /* --- Body ------------------------------- */

    /**
     * Sets the context to perform the operation of this flyweight correctly.
     * 
     * @param tag        (unique state)
     * @param cardString (repeating state)
     */
    public void context(int tag, String cardString) {
        cardSprite = SpriteFactory.getCardSprite(cardString);
        cards.put(tag, this);
    }

    /**
     * The operation of the flyweight: draws the card sprite.
     */
    public void draw() {
        cardSprite.draw(150.0, this);
    }

    /**
     * Applies the zoom-on-hover animation on this card.
     */
    private void makeZommable() {
        setOnMouseEntered(__ -> Animations.zoomIn(this, 0.5));
        setOnMouseExited(__ -> Animations.zoomOut(this, 0.5));
    }
}
