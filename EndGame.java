import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/* --- Mine ------------------------------- */

import view.GameResults;
import view.SpriteFactory;
import view.media.Animation;
import view.media.Animations;
import view.media.Sound;
import view.GUIContainer;

public class EndGame extends VBox implements AppState, GUIContainer {
    /* --- Singleton -------------------------- */

    private static EndGame instance;

    public static EndGame getInstance() {
        if (instance == null)
            instance = new EndGame();
        return instance;
    }

    private EndGame() {
        initialize();
    }

    /* --- Fields ----------------------------- */

    private JUno app; // context
    private Button playAgain;
    private Button backHome;

    /* --- Body ------------------------------- */

    private void playAgain() {
        Animation closing = Animations.NEW_GAME.get();
        closing.setStopFrame(5);
        closing.setDimensions(app.getScene().getWidth(), app.getScene().getHeight());
        Sound.BUTTON_CLICK.play(false);
        closing.play(this);
        app.changeState(InGame.getInstance());
    }

    private void goHome() {
        Sound.BUTTON_CLICK.play(false);
        app.changeState(Home.getInstance());
    }

    /* --- Visible ---------------------------- */

    @Override
    public void createElements() {
        playAgain = new Button();
        backHome = new Button();
    }

    @Override
    public void arrangeElements() {
        playAgain.getStyleClass().add("button");
        backHome.getStyleClass().add("button");

        ImageView playAgainImage = new ImageView();
        SpriteFactory.getButtonSprite("playAgain").draw(150.0, playAgainImage);
        playAgain.setGraphic(playAgainImage);

        
        ImageView backHomeImage = new ImageView();
        SpriteFactory.getButtonSprite("backHome").draw(150.0, backHomeImage);
        backHome.setGraphic(backHomeImage);

        HBox buttonsContainer = new HBox(playAgain, backHome);
        buttonsContainer.setAlignment(Pos.CENTER);

        setId("game-results");
        getChildren().addAll(GameResults.getInstance(), buttonsContainer);
        setAlignment(Pos.CENTER);
    }

    @Override
    public void applyBehaviors() {
        playAgain.setOnMouseClicked(e -> playAgain());
        backHome.setOnMouseClicked(e -> goHome());
    }

    /* --- State ------------------------------ */

    public void setContext(JUno app) {
        this.app = app;
    }

    @Override
    public void display() {
    }
}
