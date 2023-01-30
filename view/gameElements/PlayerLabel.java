package view.gameElements;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import view.SpriteFactory;

public class PlayerLabel extends HBox {
    /* --- Fields ----------------------------- */

    private ImageView icon;
    private Label nick;
    private Label cards;

    /* --- Constructors ----------------------- */

    public PlayerLabel(String avatarName, String nickname, int cardsQuantity) {
        icon = createIcon(avatarName);
        nick = createNick(nickname);
        cards = createCards(cardsQuantity);

        arrangeElements();

        getStyleClass().add("player-label");
    }

    /* --- Body ------------------------------- */
    
    private ImageView createIcon(String avatarName) {
        ImageView icon = new ImageView();
        icon.getStyleClass().add("avatar");
        SpriteFactory.getAvatarSprite(avatarName).draw(50.0, icon);
        return icon;
    }

    private Label createNick(String nickname) {
        Label nick = new Label(nickname);
        nick.getStyleClass().add("nick");
        return nick;
    }

    private Label createCards(int quantity) {
        Label cards = new Label(Integer.toString(quantity));
        cards.getStyleClass().add("cards-in-hand");
        return cards;
    }

    private void arrangeElements() {
        setSpacing(10.0);
        setAlignment(Pos.CENTER_LEFT);
        getChildren().addAll(icon, nick, cards);
    }

    protected void changeCards(int newQuantity) {
        cards.setText(Integer.toString(newQuantity));
    }

    public void modifyHandSize(int toAdd) {
        cards.setText(Integer.toString(Integer.parseInt(cards.getText()) + toAdd)); 
    }
}
