package view.gameElements;

import java.util.HashMap;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/* --- Mine ------------------------------- */

import view.SpriteFactory;
import view.CUView;
import view.GUIContainer;

import events.EventListener;
import events.Event;

/**
 * Implements the <em>Singleton</em> pattern.
 * <p>
 * Gathers all actions performed during a game, in chronological order.
 */
public class ActionsChronology extends StackPane implements EventListener, GUIContainer {
    /* --- Singleton -------------------------- */

    private static ActionsChronology instance;

    /**
     * Implements the <em>Singleton</em> pattern.
     * 
     * @return The unique instance of this class.
     */
    public static ActionsChronology getInstance() {
        if (instance == null)
            instance = new ActionsChronology();
        return instance;
    }

    private ActionsChronology() {
        CUView.getInstance().subscribe(this, Event.AI_PLAYED_CARD, Event.USER_PLAYED_CARD, Event.GAME_READY);
        
        initialize();
    }

    /* --- Fields ----------------------------- */

    private ScrollPane scrollPane;
    private HBox memories;

    /* --- Body ------------------------------- */

    public void addMemory(ImageView action, String performerIcon, String performerNickame) {
        ImageView avatar = new ImageView();
        SpriteFactory.getAvatarSprite(performerIcon).draw(50.0, avatar);
        Label nickname = new Label(performerNickame);
        VBox newMemory = new VBox(action, avatar, nickname);
        memories.getChildren().add(newMemory);
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

    /* --- GUIContainer ----------------------- */

    @Override
    public void createElements() {
        memories = new HBox();
        scrollPane = new ScrollPane(memories);
    }

    @Override
    public void arrangeElements() {
        setId("chronology");

        memories.setAlignment(Pos.CENTER_LEFT);

        scrollPane.setContent(memories);
        scrollPane.setMaxHeight(400.0);
        scrollPane.setMaxWidth(1000.0);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        getChildren().add(scrollPane);
    }

    @Override
    public void applyBehaviors() {
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER);
    }

    /* --- Observer --------------------------- */

    @Override
    public void update(Event event, HashMap<String, Object> data) {
        String cardString = (String) data.get("card-representation");
        switch (event) {
            case GAME_READY:
                Platform.runLater(() -> memories.getChildren().clear());
                break;
            case AI_PLAYED_CARD:
            case USER_PLAYED_CARD:
                Platform.runLater(() -> {
                    ImageView cardPlayed = new ImageView();
                    SpriteFactory.getCardSprite(cardString).draw(150.0, cardPlayed);
                    String icon = (String) data.get("icon");
                    String nickname = (String) data.get("nickname");
                    addMemory(cardPlayed, icon, nickname);
                });
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }
}
