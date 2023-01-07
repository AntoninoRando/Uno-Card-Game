import java.util.Timer;
import java.util.TimerTask;

import controller.Controls;
import controller.DropAndPlay;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import model.GameThread;
import model.gameEntities.Enemies;
import model.gameEntities.Player;
import model.gameLogic.Game;
import view.animations.Animation;
import view.animations.Animations;
import view.gameElements.CardChronology;
import view.gameElements.HandPane;
import view.gameElements.PlayerPane;
import view.gameElements.PlayzonePane;
import view.gameElements.SelectionPane;
import view.gameElements.TerrainPane;
import view.sounds.Sounds;

public class InGame extends StackPane implements AppState {
    /* --- Singleton -------------------------- */

    private static InGame instance;

    public static InGame getInstance() {
        if (instance == null)
            instance = new InGame();
        return instance;
    }

    private InGame() {
        createElements();
        arrangeElements();
    }

    /* --- Fields ----------------------------- */

    private JUno app; // context
    private BorderPane gameElements;
    private PlayerPane players;
    private TerrainPane terrain;
    private HandPane userHand;
    private CardChronology chronology;
    private PlayzonePane playZone;
    private SelectionPane selectionZone;
    private Timer scrollTimer;

    /* --- Body ------------------------------- */

    // TODO rendere lo scroll un control, con tanto di timer
    private void scrollChronology(ScrollEvent e) {
        if (scrollTimer != null)
            scrollTimer.cancel();

        chronology.setVisible(true);
        chronology.scroll(e.getDeltaY());

        scrollTimer = new Timer();
        scrollTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                chronology.setVisible(false);
                chronology.bringToTheEnd();
            }
        }, 2000L);
    }

    /**
     * Starts a new game.
     */
    private void newGame() {
        Player[] players = new Player[] { Enemies.JINX, Enemies.VIEGO, Enemies.XAYAH, Enemies.ZOE,
                new Player("resources\\icons\\night.png", "User") };
        Game.getInstance().setupGame(players);
        GameThread.play();

        Animation opening = Animations.NEW_GAME.get();
        opening.setStartFrame(6);
        opening.setDimensions(app.getScene().getWidth(), app.getScene().getHeight());
        // TODO forse devo usare Rectangle2D screenBounds =
        // Screen.getPrimary().getBounds();

        Sounds.IN_GAME_SOUNDTRACK.play();
        opening.play(this);
    }

    /**
     * Default ending of the game: after a player wins.
     */
    private void end() {
        // GameThread.stop(false);
        Sounds.IN_GAME_SOUNDTRACK.stop();
        app.changeState(EndGame.getInstance());
    }

    /**
     * Unconventional ending of the game: the user click the quit button.
     */
    private void quit() {
        // GameThread.stop(true);
        Sounds.IN_GAME_SOUNDTRACK.stop();
    }

    /* --- State ------------------------------ */

    public void setContext(JUno app) {
        this.app = app;
    }

    @Override
    public void createElements() {
        setId("background");

        gameElements = new BorderPane();
        players = PlayerPane.getInstance();
        terrain = TerrainPane.getInstance();
        chronology = CardChronology.getInstance();
        userHand = HandPane.getInstance();
        playZone = PlayzonePane.getInstance();
        selectionZone = SelectionPane.getInstance();

        playZone.setOnScroll(this::scrollChronology);
        DropAndPlay.setPlayzone(playZone);
        Controls.draw.apply(playZone);
        Controls.uno.apply(playZone);
    }

    @Override
    public void arrangeElements() {
        setId("background");

        StackPane cardZone = new StackPane(terrain, chronology);
        CardChronology.getInstance().setVisible(false);

        Region padderRegionRight = new Region();
        padderRegionRight.prefWidthProperty().bind(players.widthProperty());

        gameElements.setCenter(cardZone);
        gameElements.setLeft(players);
        gameElements.setBottom(userHand);
        gameElements.setRight(padderRegionRight);

        getChildren().addAll(gameElements, playZone, selectionZone);
    }

    @Override
    public void display() {
        newGame();
    }
}
