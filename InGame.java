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

import model.GameThread;
import model.gameEntities.Enemies;
import model.gameEntities.Player;
import model.gameLogic.Game;
import view.CUView;
import view.Visible;
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

public class InGame extends StackPane implements AppState, EventListener, Visible {
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
        CUView.getInstance().subscribe(this, Event.GAME_END, Event.PLAYER_PLAYED_CARD, Event.INVALID_CARD,
                Event.TURN_BLOCKED, Event.TURN_START);
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

        SettingsMenu.getInstance().addOptions(quit, restart);

        Animation opening = Animations.NEW_GAME.get();
        opening.setStartFrame(6);
        opening.setDimensions(app.getScene().getWidth(), app.getScene().getHeight());

        Sounds.IN_GAME_SOUNDTRACK.play();
        opening.play(this);
    }

    /**
     * Default ending of the game (i.e., a player won).
     */
    private void end(boolean isInterrupt) {
        GameThread.stop(isInterrupt);
        Sounds.IN_GAME_SOUNDTRACK.stop();
        SettingsMenu.getInstance().removeOptions();
    }

    private void restart() {
        end(true);
        newGame();
    }

    private void quit(boolean isInterrupted) {
        end(isInterrupted);

        if (isInterrupted) {
            Home.getInstance().setContext(app);
            app.changeState(Home.getInstance());
        } else {
            EndGame.getInstance().setContext(app);
            app.changeState(EndGame.getInstance());
        }
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

        playZone.setOnScroll(this::scrollChronology);
        DropAndPlay.setPlayzone(playZone);
        Controls.draw.apply(playZone);
        Controls.uno.apply(playZone);

        restart.setOnMouseClicked(e -> {
            Sounds.BUTTON_CLICK.play();
            SettingsMenu.getInstance().setVisible(false);
            restart();
        });
        quit.setOnMouseClicked(e -> {
            Sounds.BUTTON_CLICK.play();
            SettingsMenu.getInstance().setVisible(false);
            quit(true);
        });
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
            case GAME_END:
                Platform.runLater(() -> quit(false));
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
            case INVALID_CARD:
                Platform.runLater(() -> ResetTranslate.resetTranslate(Card.cards.get((int) data.get("card-tag"))));
                break;
            // case UNO_DECLARED:
            // Platform.runLater(() -> {
            // unoAnimation.setSceneCoordinates(scene.getWidth() / 2 - 200,
            // scene.getHeight() / 2 - 111.85);
            // unoAnimation.play(gameRoot);
            // });
            // break;
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
