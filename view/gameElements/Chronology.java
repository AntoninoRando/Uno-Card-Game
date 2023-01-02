package view.gameElements;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

/* --- Mine ------------------------------- */

import events.toView.EventListener;
import events.toView.EventType;

import prefabs.Card;
import prefabs.Player;
import prefabs.Suit;

/*
 * A pane that gather all actions performed during the game, in order from the first to the most recent.
 */
public class Chronology extends StackPane implements EventListener {
    /* --- Singleton -------------------------- */

    private static Chronology instance;

    public static Chronology getInstance() {
        if (instance == null)
            instance = new Chronology();
        return instance;
    }

    private Chronology() {
        ScrollPane sp = new ScrollPane(content);
        getChildren().add(sp);
        addStyle();
    }

    /* --- Fields ----------------------------- */

    private int lastMem; // The VALUE of the most recent card played.
    private GridPane content = new GridPane();

    /* --- Body ------------------------------- */

    /**
     * Add the card just played to the memory.
     * 
     * @param card The image of the card played, that is the GUI card.
     */
    private void addCard(CardContainer card) {
        GridPane.setRowIndex(card, 0);
        GridPane.setColumnIndex(card, lastMem);
        content.getChildren().add(card);
        lastMem++;
    }

    /**
     * Add the icon of the player who performed the action.
     * 
     * @param icon The image path to the icon.
     */
    private void addIcon(String icon) {
        Circle avatar = new Circle(30, new ImagePattern(new Image(icon)));
        GridPane.setRowIndex(avatar, 1);
        GridPane.setColumnIndex(avatar, lastMem - 1);
        content.getChildren().add(avatar);
    }

    /**
     * Add the nickname of the player who performed the action.
     * 
     * @param nick The nickname of the player.
     */
    private void addNick(String nick) {
        Label nickL = new Label(nick);
        GridPane.setRowIndex(nickL, 3);
        GridPane.setColumnIndex(nickL, lastMem - 1);
        content.getChildren().add(nickL);
    }

    /**
     * Add the style to the chronology pane.
     */
    private void addStyle() {
        ScrollPane sp = (ScrollPane) getChildren().get(0);
        sp.setMaxHeight(400.0);
        sp.setMaxWidth(1000.0);
        sp.setFitToHeight(true);
        sp.setFitToWidth(true);
        sp.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        sp.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);

        content.setAlignment(Pos.CENTER_LEFT);

        setId("chronology");
    }

    /**
     * Scroll through the pane to see al the chronology.
     * 
     * @param deltaY The scroll amount: if positive it goes forward, otherwise it
     *               goes backward.
     */
    public void scroll(double deltaY) {
        ScrollPane sp = (ScrollPane) getChildren().get(0);
        sp.setHvalue(sp.getHvalue() + (deltaY < 0 ? 0.1 : -0.1));
    }

    /**
     * Return the scroll to the origin, that is the first action of the game.
     * TODO dovrebbe riportarlo alla fine, non all'origine.
     */
    public void bringToTheEnd() {
        ScrollPane sp = (ScrollPane) getChildren().get(0);
        sp.setHvalue(sp.getHmax());
    }

    /* --- Observer --------------------------- */

    @Override
    public void update(EventType event, Card data) {
        switch (event) {
            case CARD_CHANGE:
                Platform.runLater(() -> addCard(data.getGuiContainer()));
                break;
            default:
                // TODO risolvere throwUnsupportedError(event, data);
                break;
        }
    }

    @Override
    public void update(EventType event, Player data) {
        switch (event) {
            case PLAYER_PLAYED_CARD:
                Platform.runLater(() -> {
                    addIcon(data.getIcon());
                    addNick(data.getNick());
                });
                break;
            case TURN_BLOCKED:
                Platform.runLater(() -> {
                    addCard(new Card(Suit.WILD, -2).getGuiContainer());
                    addIcon(data.getIcon());
                    addNick(data.getNick());
                });
                break;
            case PLAYER_DREW:
                Platform.runLater(() -> {
                    addCard(new Card(Suit.WILD, -1).getGuiContainer());
                    addIcon(data.getIcon());
                    addNick(data.getNick());
                });
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }

    @Override
    public void update(EventType event) {
        switch (event) {
            case RESET:
                Platform.runLater(() -> {
                    content = new GridPane();
                    ScrollPane sp = (ScrollPane) getChildren().get(0);
                    sp.setContent(content);
                });
                break;
            default:
                throwUnsupportedError(event, null);
        }
    }
}