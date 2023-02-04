import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/* --- Mine ------------------------------- */

import controller.Controls;
import controller.DropAndPlay;

import events.Event;
import events.EventListener;
import model.gameLogic.GameExecuter;
import view.CUView;
import view.GUIContainer;
import view.gameElements.Card;
import view.gameElements.ActionsChronology;
import view.gameElements.HandPane;
import view.gameElements.PlayerLabel;
import view.gameElements.PlayerPane;
import view.gameElements.PlayzonePane;
import view.gameElements.SelectionPane;
import view.gameElements.TerrainPane;
import view.media.Animation;
import view.media.AnimationHandler;
import view.media.AnimationLayer;
import view.media.Animations;
import view.media.Sound;
import view.settings.SettingsMenu;

public class InGame extends StackPane implements AppState, EventListener, GUIContainer, AnimationLayer {
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
        applyBehaviors();
        CUView.getInstance().subscribe(this, Event.INVALID_CARD, Event.GAME_START, Event.PLAYER_WON);
        AnimationHandler.subscribe(this, Event.UNO_DECLARED, Event.AI_PLAYED_CARD, Event.TURN_START, Event.TURN_BLOCKED);
    }

    /* --- Fields ----------------------------- */

    private JUno app; // context
    private BorderPane gameElements;
    private PlayerPane players;
    private TerrainPane terrain;
    private HandPane userHand;
    private ActionsChronology chronology;
    private PlayzonePane playZone;
    private SelectionPane selectionZone;
    private Timer scrollTimer;
    private Button restart;
    private Button quit;
    private Animation closingAnimation;
    private Animation openingAnimation;

    /* --- Body ------------------------------- */

    // TODO rendere lo scroll un control, con tanto di timer
    private void scrollChronology(ScrollEvent e) {
        if (scrollTimer != null) {
            scrollTimer.cancel();
            scrollTimer.purge();
        }

        chronology.setVisible(true);
        chronology.scroll(e.getDeltaY());

        scrollTimer = new Timer();
        scrollTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                chronology.setVisible(false);
                chronology.bringToTheEnd();
                scrollTimer.cancel();
                scrollTimer.purge();
            }
        }, 2000L);
    }

    /**
     * Starts a new game.
     */
    private void newGame() {
        SettingsMenu.getInstance().addOptions(quit, restart);

        closingAnimation.setDimensions(app.getScene().getWidth(), app.getScene().getHeight());
        closingAnimation.setOnFinishAction(e -> GameExecuter.playNewGame());
        closingAnimation.play(this);

        Sound.IN_GAME_SOUNDTRACK.play(true);
    }

    private void restart() {
        Sound.IN_GAME_SOUNDTRACK.stop();
        SettingsMenu.getInstance().removeOptions();
        newGame();
    }

    private void quit() {
        GameExecuter.stop();
        Sound.IN_GAME_SOUNDTRACK.stop();
        SettingsMenu.getInstance().removeOptions();

        Home.getInstance().setContext(app);
        app.changeState(Home.getInstance());
    }

    private void displayResults() {
        GameExecuter.stop();
        Sound.IN_GAME_SOUNDTRACK.stop();
        SettingsMenu.getInstance().removeOptions();

        EndGame.getInstance().setContext(app);
        app.changeState(EndGame.getInstance());
    }

    /* --- Visible ---------------------------- */

    @Override
    public void createElements() {
        setId("background");

        gameElements = new BorderPane();
        players = PlayerPane.getInstance();
        terrain = TerrainPane.getInstance();
        chronology = ActionsChronology.getInstance();
        userHand = HandPane.getInstance();
        playZone = PlayzonePane.getInstance();
        selectionZone = SelectionPane.getInstance();
        restart = new Button("Restart");
        quit = new Button("Quit game");

        closingAnimation = Animations.NEW_GAME.get();
        closingAnimation.setStopFrame(5);
        closingAnimation.setWillStay(true);

        openingAnimation = Animations.NEW_GAME.get();
        openingAnimation.setStartFrame(6);
    }

    @Override
    public void arrangeElements() {
        setId("background");

        StackPane cardZone = new StackPane(terrain, chronology);
        ActionsChronology.getInstance().setVisible(false);

        Region padderRegionRight = new Region();
        padderRegionRight.prefWidthProperty().bind(players.widthProperty());

        gameElements.setCenter(cardZone);
        gameElements.setLeft(players);
        gameElements.setBottom(userHand);
        gameElements.setRight(padderRegionRight);

        getChildren().addAll(gameElements, playZone, selectionZone);
    }

    @Override
    public void applyBehaviors() {
        playZone.setOnScroll(this::scrollChronology);
        DropAndPlay.setPlayzone(playZone);
        Controls.applyDrawControl(playZone);
        Controls.applyUnoControl(playZone);

        restart.setOnMouseClicked(e -> {
            Sound.BUTTON_CLICK.play(false);
            SettingsMenu.getInstance().setVisible(false);
            restart();
        });
        quit.setOnMouseClicked(e -> {
            Sound.BUTTON_CLICK.play(false);
            SettingsMenu.getInstance().setVisible(false);
            quit();
        });
    }

    /* --- State ------------------------------ */

    public void setContext(JUno app) {
        this.app = app;
    }

    @Override
    public void display() {
        newGame();
    }

    /* --- Observer --------------------------- */

    @Override
    public void update(Event event, Map<String, Object> data) {
        switch (event) {
            case GAME_START:
                openingAnimation.setDimensions(app.getScene().getWidth(), app.getScene().getHeight());
                Platform.runLater(() -> openingAnimation.play(this));
                break;
            case PLAYER_WON:
                Platform.runLater(() -> displayResults());
                break;
            case INVALID_CARD:
                Platform.runLater(() -> Animations.resetTranslate(Card.cards.get((int) data.get("card-identifier"))));
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }

    @Override
    public Entry<Pane, Double[]> getPoints(Event event) {
        Double x = null, y = null, w = null, h = null;

        if (event.equals(Event.UNO_DECLARED))
            w = 300.0;
        else if (event.equals(Event.AI_PLAYED_CARD))
            w = 400.0;
        else if (event.equals(Event.TURN_BLOCKED))
            w = 200.0;
        else if (event.equals(Event.TURN_START)) {
            PlayerLabel playerLabel = PlayerPane.getInstance().getPlayerLabel();
            Bounds labelBounds = playerLabel.localToScene(playerLabel.getBoundsInLocal());

            x = 0.0;
            y = labelBounds.getMinY() - 5;
            w = players.getWidth();
            h = labelBounds.getHeight() + 10;
        }

        return Map.entry(this, new Double[] { x, y, w, h });
    }

}
