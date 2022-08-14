package view.gameElements;

import javafx.geometry.Pos;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class PlayerLabel extends HBox {
    private Circle icon;
    private Label nick;
    private Label cards;

    public PlayerLabel(String imagePathname, String nickname, int cardsQuantity) {
        icon = createIcon(new Image(imagePathname));
        nick = createNick(nickname);
        cards = createCards(cardsQuantity);

        arrangeElements();

        getStyleClass().add("player-label");
    }

    private Circle createIcon(Image img) {
        Circle icon = new Circle(20, 20, 20);
        icon.setFill(new ImagePattern(img));
        icon.getStyleClass().add("avatar");
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
}
