package view.prefabs;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

/*
 * A pane that gather all actions performed during the game, in order from the first to the most recent.
 */
public class Chronology extends StackPane {
    /* --- Fields ----------------------------- */

    protected HBox content;
    protected Node lastRepresentation;
    protected String lastPerformerIcon;
    protected String lastPerformerNick;

    /* --- Constructor ------------------------ */

    protected Chronology() {
        this.content = new HBox();
        ScrollPane scrollPane = new ScrollPane(content);
        getChildren().add(scrollPane);
        addStyle();
    }

    /* --- Body ------------------------------- */

    public void addMemoryInfo(Node representation, String performerIcon, String performerNick) {
        lastRepresentation = representation;
        lastPerformerIcon = performerIcon;
        lastPerformerNick = performerNick;
    }

    public void update() {
        content.getChildren().add(new Memory(lastRepresentation, lastPerformerIcon, lastPerformerNick));
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
     * Return the scroll to the rightmost end.
     */
    public void bringToTheEnd() {
        ScrollPane sp = (ScrollPane) getChildren().get(0);
        sp.setHvalue(sp.getHmax());
    }
}

class Memory extends VBox {
    Node representation;
    Circle performerAvatar;
    Label performerLabel;

    Memory(Node representation, String performerIcon, String performerNickname) {
        this.representation = representation;
        this.performerAvatar = avatarOf(performerIcon);
        this.performerLabel = labelOf(performerNickname);
        getChildren().addAll(representation, performerAvatar, performerLabel);
    }

    /**
     * Add the icon of the player who performed the action.
     * 
     * @param icon The image path to the icon.
     */
    private Circle avatarOf(String icon) {
        return new Circle(30, new ImagePattern(new Image(icon)));
    }

    /**
     * Add the nickname of the player who performed the action.
     * 
     * @param nick The nickname of the player.
     */
    private Label labelOf(String nick) {
        return new Label(nick);
    }
}