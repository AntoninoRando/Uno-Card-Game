package view.gameElements;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/* --- JUno ------------------------------- */

import view.SpriteFactory;
import view.GUIContainer;

/**
 * A box that shows the player nickname, icon and the number of remaining cards.
 */
public class PlayerLabel extends HBox implements GUIContainer {
    /* --- Fields ----------------------------- */

    private ImageView icon;
    private Label nick;
    private Label cards;

    /* --- Constructors ----------------------- */

    public PlayerLabel(String avatarName, String nickname, int cardsQuantity) {
        initialize();

        SpriteFactory.getAvatarSprite(avatarName).draw(50.0, icon);
        nick.setText(nickname);
        cards.setText(Integer.toString(cardsQuantity));
    }

    /* --- Body ------------------------------- */

    /**
     * Increases (or decreases) the (label) number of cards in hand by the given
     * amount.
     * 
     * @param toAdd The amount to add or subtract (if negative).
     */
    public void modifyHandSize(int toAdd) {
        cards.setText(Integer.toString(Integer.parseInt(cards.getText()) + toAdd));
    }

    /* --- GUIContainer ------------------------ */

    @Override
    public void createElements() {
        icon = new ImageView();
        nick = new Label();
        cards = new Label();
    }

    @Override
    public void arrangeElements() {
        getStyleClass().add("player-label");
        setSpacing(10.0);
        setAlignment(Pos.CENTER_LEFT);
        getChildren().addAll(icon, nick, cards);

        icon.getStyleClass().add("avatar");
        nick.getStyleClass().add("nick");
        cards.getStyleClass().add("cards-in-hand");
    }

    @Override
    public void applyBehaviors() {
    }
}
