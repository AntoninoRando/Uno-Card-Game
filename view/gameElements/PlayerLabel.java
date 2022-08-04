package view.gameElements;

import java.util.HashMap;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class PlayerLabel extends HBox {
    private Circle icon;
    private Label nick;
    private Label cards;
    protected static HashMap<String, String> botIcons = fillBotIcons();

    public PlayerLabel(String imagePathname, String nickname, int cardsQuantity) {
        icon = createIcon(new Image(imagePathname));
        nick = createNick(nickname);
        cards = createCards(cardsQuantity);

        getStyleClass().add("player-label");

        arrangeElements();
    }

    private static HashMap<String, String> fillBotIcons() {
        HashMap<String, String> map = new HashMap<>();
        map.put("Top Princessess", "resources\\icons\\queen.png");
        map.put("Bot Luca", "resources\\icons\\blood.png");
        map.put("Bot Giovanni", "resources\\icons\\tree.png");
        return map;
    }

    private Circle createIcon(Image img) {
        Circle icon = new Circle(20, 20, 20);
        icon.setFill(new ImagePattern(img));
        return icon;
    }

    private Label createNick(String nickname) {
        Label nick = new Label(nickname);
        return nick;
    }

    private Label createCards(int quantity) {
        Label cards = new Label(Integer.toString(quantity));
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

    protected void focusPlayer() {
        nick.setTextFill(Color.color(1, 0, 0));
    }

    protected void unfocusPlayer() {
        nick.setTextFill(Color.color(0, 0, 0));
    }
}