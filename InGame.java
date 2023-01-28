import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/* --- Mine ------------------------------- */

import controller.Controls;
import controller.DropAndPlay;

import events.Event;
import events.EventListener;
import model.gameEntities.Enemies;
import model.gameEntities.Player;
import model.gameLogic.GameThread;
import view.CUView;
import view.GUIContainer;
import view.animations.Animation;
import view.animations.Animations;
import view.animations.ResetTranslate;
import view.gameElements.Card;
import view.gameElements.CardChronology;
import view.gameElements.HandPane;
import view.gameElements.PlayerLabel;
import view.gameElements.PlayerPane;
import view.gameElements.PlayzonePane;
import view.gameElements.SelectionPane;
import view.gameElements.TerrainPane;
import view.settings.SettingsMenu;
import view.sounds.Sounds;

public class InGame extends StackPane implements AppState, EventListener, GUIContainer {
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
        CUView.getInstance().subscribe(this, Event.PLAYER_PLAYED_CARD, Event.INVALID_CARD,
                Event.TURN_BLOCKED, Event.TURN_START, Event.GAME_START, Event.PLAYER_WON, Event.UNO_DECLARED);
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
        Player[] players = new Player[] { new Player(), Enemies.JINX, Enemies.VIEGO, Enemies.XAYAH, Enemies.ZOE };

        SettingsMenu.getInstance().addOptions(quit, restart);

        closingAnimation.setDimensions(app.getScene().getWidth(), app.getScene().getHeight());
        closingAnimation.setOnFinishAction(e -> GameThread.play(players));
        closingAnimation.play(this);

        Sounds.IN_GAME_SOUNDTRACK.play();
    }

    private void restart() {
        Sounds.IN_GAME_SOUNDTRACK.stop();
        SettingsMenu.getInstance().removeOptions();
        newGame();
    }

    private void quit() {
        GameThread.stop(true);
        Sounds.IN_GAME_SOUNDTRACK.stop();
        SettingsMenu.getInstance().removeOptions();

        Home.getInstance().setContext(app);
        app.changeState(Home.getInstance());
    }

    private void displayResults() {
        GameThread.stop(true);
        Sounds.IN_GAME_SOUNDTRACK.stop();
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
        chronology = CardChronology.getInstance();
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
    public void applyBehaviors() {
        playZone.setOnScroll(this::scrollChronology);
        DropAndPlay.setPlayzone(playZone);
        Controls.draw.apply(playZone);
        Controls.UNO.apply(playZone);

        restart.setOnMouseClicked(e -> {
            Sounds.BUTTON_CLICK.play();
            SettingsMenu.getInstance().setVisible(false);
            restart();
        });
        quit.setOnMouseClicked(e -> {
            Sounds.BUTTON_CLICK.play();
            SettingsMenu.getInstance().setVisible(false);
            quit();
        });

        Controls.SKIP.apply(restart);
        Controls.SKIP.apply(quit);
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
    public void update(Event event, HashMap<String, Object> data) {
        switch (event) {
            case GAME_START:
                openingAnimation.setDimensions(app.getScene().getWidth(), app.getScene().getHeight());
                Platform.runLater(() -> openingAnimation.play(this));
                break;
            case PLAYER_WON:
                Platform.runLater(() -> displayResults());
                break;
            case TURN_START:
                Animation focusPlayerOnTurnAnimation = Animations.FOCUS_PLAYER.get();
                focusPlayerOnTurnAnimation.setWillCountdown(true);
                focusPlayerOnTurnAnimation.setWillStay(true);

                Platform.runLater(() -> {
                    String nick = (String) data.get("nickname");
                    PlayerLabel playerLabel = PlayerPane.getInstance().getPlayerLabel(nick);
                    Bounds labelBounds = playerLabel.localToScene(playerLabel.getBoundsInLocal());

                    focusPlayerOnTurnAnimation.setDimensions(players.getWidth(), labelBounds.getHeight() + 10);
                    focusPlayerOnTurnAnimation.setSceneCoordinates(0.0, labelBounds.getMinY() - 5);
                    focusPlayerOnTurnAnimation.play(this).getValue().getStyleClass().add("fleeting");
                });

                try {
                    focusPlayerOnTurnAnimation.latch.await();
                } catch (InterruptedException e) {
                }
                focusPlayerOnTurnAnimation.resetLatch();
                break;
            case TURN_BLOCKED:
                Animation blockTurnAnimation = Animations.BLOCK_TURN.get();

                Platform.runLater(() -> {
                    blockTurnAnimation.setSceneCoordinates(app.getScene().getWidth() / 2,
                            app.getScene().getHeight() / 2);
                    blockTurnAnimation.setDimensions(200.0, null);
                    blockTurnAnimation.setWillCountdown(true);
                    blockTurnAnimation.play(this);
                });

                try {
                    blockTurnAnimation.latch.await();
                } catch (InterruptedException e) {
                }
                blockTurnAnimation.resetLatch();
                break;
            case PLAYER_PLAYED_CARD:
                Animation playingCardAnimation = Animations.CARD_PLAYED.get();

                Platform.runLater(() -> {
                    playingCardAnimation.setDimensions(400.0, null);
                    playingCardAnimation.setWillCountdown(true);
                    playingCardAnimation.setSceneCoordinates(app.getScene().getWidth() / 2,
                            app.getScene().getHeight() / 2);
                    playingCardAnimation.play(this);
                });

                try {
                    playingCardAnimation.latch.await();
                } catch (InterruptedException e) {
                }
                playingCardAnimation.resetLatch();
                break;
            case UNO_DECLARED:
                Platform.runLater(() -> {
                    boolean said = (boolean) data.get("said");

                    if (said) {
                        Animation unoAnimation = Animations.UNO_TEXT.get();
                        unoAnimation.setDimensions(null, app.getScene().getHeight() / 4);
                        unoAnimation.setSceneCoordinates(app.getScene().getWidth() / 2,
                                app.getScene().getHeight() / 2);
                        unoAnimation.play(this);
                    } else {
                        // TODO scegliere un'altra animazione
                        Animation missed = Animations.BLOCK_TURN.get();
                        missed.setSceneCoordinates(app.getScene().getWidth() / 2,
                                app.getScene().getHeight() / 2);
                        missed.setDimensions(200.0, null);
                        missed.setWillCountdown(true);
                        missed.play(this);
                    }
                });
                break;
            case INVALID_CARD:
                Platform.runLater(() -> ResetTranslate.resetTranslate(Card.cards.get((int) data.get("card-tag"))));
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }

    @Override
    public int getEventPriority(Event event) {
        switch (event) {
            case PLAYER_PLAYED_CARD:
                return 2;
            case TURN_START:
                return -1;
            default:
                return 1;
        }
    }

}
