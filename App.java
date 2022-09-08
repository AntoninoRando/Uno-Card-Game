import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import controller.actions.UserProfileActions;
import controller.controls.Controller;
import controller.controls.ControllerFX;
import controller.controls.Controls;
import events.EventType;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import model.gameLogic.Loop;
import prefabs.Card;
import prefabs.Player;
import model.data.Info;
import model.data.PlayerData;

import view.Displayer;
import view.animations.Animation;
import view.animations.Animations;
import view.animations.ResetTranslate;
import view.endGame.EndGameSettings;
import view.gameElements.PlayerPane;
import view.gameElements.Chronology;
import view.gameElements.HandPane;
import view.gameElements.PlayerLabel;
import view.gameElements.PlayzonePane;
import view.gameElements.SelectionPane;
import view.gameElements.TerrainPane;
import view.home.HomeMenu;
import view.home.Homes;
import view.settings.Settings;
import view.sounds.Sounds;

public class App extends Displayer {
    private Scene scene;

    private StackPane rootContainer = new StackPane();

    private StackPane homeRoot;
    private HomeMenu home;

    private StackPane gameRoot;
    private StackPane gameElements;
    private Thread gameThread;

    private StackPane endGameRoot;

    public App() {
        super(EventType.GAME_READY, EventType.GAME_START, EventType.CARD_CHANGE, EventType.UNO_DECLARED,
                EventType.TURN_BLOCKED,
                EventType.TURN_END, EventType.INVALID_CARD, EventType.TURN_START, EventType.PLAYER_WON,
                EventType.XP_EARNED);

        scene = new Scene(rootContainer, 1000, 600);
        homeRoot = newHomeRoot();
        gameRoot = newGameRoot();
        endGameRoot = newEndGameRoot();
        changeRoot(homeRoot);

        scene.getStylesheets().add(getClass().getResource("resources\\Style.css").toExternalForm());
    }

    // ROOTS

    private void changeRoot(Pane newRoot) {
        rootContainer.getChildren().setAll(newRoot);
    }

    private StackPane newHomeRoot() {
        StackPane root = new StackPane();

        HomeMenu home = HomeMenu.getInstance();
        Homes.setProfileAction(e -> {
            Sounds.BUTTON_CLICK.play();
            Settings.PROFILE.setVisible(!Settings.PROFILE.isVisible());
        });
        Homes.setPlayButtonAction(e -> {
            Sounds.BUTTON_CLICK.play();
            newGame();
        });

        root.getChildren().addAll(home, Settings.PROFILE, Settings.AVATAR_PICKER);
        StackPane.setAlignment(home, Pos.TOP_LEFT);

        initializeProfile(home);

        return root;
    }

    private void initializeProfile(Pane settingsContainer) {
        Settings.PROFILE.setVisible(false);
        Settings.AVATAR_PICKER.setVisible(false);

        Info.events.subscribe(Settings.PROFILE, EventType.USER_NEW_NICK, EventType.USER_NEW_ICON, EventType.XP_EARNED,
                EventType.USER_PLAYED_GAME, EventType.USER_WON, EventType.LEVELED_UP);

        Settings.AVATAR_PICKER.onChoice(icoPath -> UserProfileActions.changeIcon(icoPath));
        try {
            Settings.AVATAR_PICKER.addOptions(Info.allIcons());
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        Settings.setDeleteAccountAction(e -> PlayerData.reset());
        Settings.setAvatarClickAction(e -> Settings.AVATAR_PICKER.setVisible(!Settings.AVATAR_PICKER.isVisible()));
        Settings.setCloseProfileOnClickOutside(settingsContainer);

        // Setting the first values
        Settings.PROFILE.update(EventType.USER_NEW_NICK, PlayerData.getUserNick());
        Settings.PROFILE.update(EventType.USER_NEW_ICON, PlayerData.getUserIcon());
        Settings.PROFILE.update(EventType.LEVELED_UP, PlayerData.getLevel());
        Settings.PROFILE.update(EventType.USER_PLAYED_GAME, PlayerData.getGames());
        Settings.PROFILE.update(EventType.USER_WON, (int) PlayerData.getWins());
        Settings.PROFILE.update(EventType.XP_EARNED, PlayerData.getXp());
    }

    private Timer scrollTimer;

    private StackPane newGameRoot() {
        StackPane root = new StackPane();
        root.setId("background");

        BorderPane gameElements = new BorderPane();
        gameElements.setLeft(PlayerPane.getInstance());
        gameElements.setBottom(HandPane.getInstance());

        StackPane center = new StackPane(TerrainPane.getInstance(), Chronology.getInstance());
        Chronology.getInstance().setVisible(false);
        gameElements.setCenter(center);

        Region padderRegionRight = new Region();
        padderRegionRight.prefWidthProperty().bind(PlayerPane.getInstance().widthProperty());
        gameElements.setRight(padderRegionRight);

        root.getChildren().addAll(gameElements, PlayzonePane.getInstance(), SelectionPane.getInstance());

        PlayzonePane.getInstance().setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.SECONDARY))
                Controls.DECLARE_UNO.getAction().handle(e);
            else
                Controls.DRAW.getAction().handle(e);
        });
        PlayzonePane.getInstance().setOnScroll(e -> {
            if (scrollTimer != null)
                scrollTimer.cancel();

            Chronology.getInstance().setVisible(true);
            Chronology.getInstance().scroll(e.getDeltaY());

            scrollTimer = new Timer();
            scrollTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Chronology.getInstance().setVisible(false);
                    Chronology.getInstance().bringToTheEnd();
                }
            }, 2000L);
        });

        addSettingsContents(root);

        return root;
    }

    private StackPane newEndGameRoot() {
        Button playAgain = new Button();
        playAgain.setOnMouseClicked(e -> {
            Sounds.BUTTON_CLICK.play();
            newGame();
        });
        playAgain.setStyle("-fx-background-color: none");

        ImageView icon = new ImageView(new Image("resources\\BlueButton.png"));
        icon.setPreserveRatio(true);
        icon.setFitWidth(150.0);
        playAgain.setGraphic(icon);

        Button backHome = new Button();
        backHome.setOnMouseClicked(e -> {
            Sounds.BUTTON_CLICK.play();
            changeRoot(homeRoot);
            gameElements.setVisible(false);
            home.setVisible(true);
        });
        backHome.setStyle("-fx-background-color: none");
        ImageView icon2 = new ImageView(new Image("resources\\HomeButton.png"));
        icon2.setPreserveRatio(true);
        icon2.setFitWidth(150.0);
        backHome.setGraphic(icon2);

        EndGameSettings.addButtons(playAgain, backHome);

        return new StackPane(EndGameSettings.GAME_RESULTS);
    }

    private void addSettingsContents(Pane root) {
        Loop.events.subscribe(Settings.MENU.settings, EventType.GAME_START, EventType.RESET);
        root.getChildren().addAll(Settings.MENU, Settings.SETTINGS_BUTTON);
        StackPane.setAlignment(Settings.SETTINGS_BUTTON, Pos.TOP_RIGHT);

        Settings.setRestartButtonAction(e -> {
            endGame();
            newGame();
        });
        Settings.setQuitButtonAction(e -> {
            endGame();
            changeRoot(homeRoot);
        });
    }

    /* ANIMATIONS */

    private Animation unoAnimation;
    private Animation blockTurnAnimation;
    private Animation playingCardAnimation;
    private Animation focusPlayerOnTurnAnimation;
    private Animation a1 = Animations.NEW_GAME.get();
    private Animation a2 = Animations.NEW_GAME.get();

    private void loadMedia() {
        Animations.FOCUS_PLAYER.get().load();
        Animations.NEW_GAME.get().load();

        unoAnimation = Animations.UNO_TEXT.get();
        unoAnimation.setDimensions(400.0, null);

        blockTurnAnimation = Animations.BLOCK_TURN.get();
        blockTurnAnimation.setSceneCoordinates(scene.getWidth() / 2, scene.getHeight() / 2);
        blockTurnAnimation.setDimensions(200.0, null);
        blockTurnAnimation.setWillCountdown(true);

        playingCardAnimation = Animations.CARD_PLAYED.get();
        playingCardAnimation.setDimensions(400.0, null);
        playingCardAnimation.setWillCountdown(true);

        focusPlayerOnTurnAnimation = Animations.FOCUS_PLAYER.get();
        focusPlayerOnTurnAnimation.setWillCountdown(true);
        focusPlayerOnTurnAnimation.setWillStay(true);

        a1.setStopFrame(5);
        a1.setWillStay(true);
        a1.setOnFinishAction(e -> {
            gameThread = new Thread(() -> Loop.getInstance().play());
            gameThread.start();

            rootContainer.getChildren().remove(0);
            rootContainer.getChildren().add(0, gameRoot);
        });

        a2.setStartFrame(6);

        Sounds.loadAll();
    }

    /* GAME INITIALIZATION */

    private void newGame() {
        Player p1 = new Player("resources/Data/userInfo.txt");
        Player p2 = new Player("resources/Data/BotTopPrincessess.txt");
        Player p3 = new Player("resources/Data/BotLuca.txt");
        Player p4 = new Player("resources/Data/BotGiorgio.txt");

        Controller c1 = new ControllerFX();
        c1.setSource(p1);

        TreeMap<Integer, Player> players = new TreeMap<>();
        players.put(0, p1);
        players.put(1, p2);
        players.put(2, p3);
        players.put(3, p4);

        Loop.getInstance().setupGame(players, c1);

        Rectangle2D screenB = Screen.getPrimary().getBounds();
        a1.setDimensions(screenB.getWidth(), screenB.getHeight());
        a2.setDimensions(screenB.getWidth(), screenB.getHeight());

        a1.play(rootContainer);
    }

    private void endGame() {
        Sounds.IN_GAME_SOUNDTRACK.stop();
        Loop.getInstance().endGame(true);
        gameThread.interrupt();
    }

    // CONTROLLERS
    private void setupControllers() {
        UserProfileActions.setActionListener(PlayerData.getThisForStaticCalls());
    }

    /* EVENT LISTENER METHODS */

    // TODO le animazioni le gioca toppo on the top: stanno sopra al menù di pausa

    @Override
    public void update(EventType event) {
        switch (event) {
            case UNO_DECLARED:
                Platform.runLater(() -> {
                    unoAnimation.setSceneCoordinates(scene.getWidth() / 2 - 200, scene.getHeight() / 2 - 111.85);
                    unoAnimation.play(gameRoot);
                });
                break;
            default:
                throwUnsupportedError(event, null);
        }
    }

    @Override
    public void update(EventType event, Player[] data) {
        switch (event) {
            case GAME_READY:
                Platform.runLater(() -> {
                    int i = 0;
                    while (i < gameRoot.getChildren().size()) {
                        if (gameRoot.getChildren().get(i).getStyleClass().contains("fleeting"))
                            gameRoot.getChildren().remove(i);
                        else
                            i += 1;
                    }
                });
                break;
            case GAME_START:
                Platform.runLater(() -> {
                    Sounds.IN_GAME_SOUNDTRACK.play();
                    a2.play(rootContainer);
                });
                break;
            default:
                throwUnsupportedError(event, null);
        }
    }

    @Override
    public void update(EventType event, Card data) {
        switch (event) {
            case CARD_CHANGE:
                Platform.runLater(() -> {
                    playingCardAnimation.setSceneCoordinates(scene.getWidth() / 2, scene.getHeight() / 2);
                    playingCardAnimation.play(gameRoot, gameRoot.getChildren().size() - 2);
                });
                try {
                    playingCardAnimation.latch.await();
                } catch (InterruptedException e) {
                }
                playingCardAnimation.resetLatch();
                break;
            case INVALID_CARD:
                Platform.runLater(() -> ResetTranslate.resetTranslate(data.getGuiContainer()));
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }

    @Override
    public void update(EventType event, Player data) {
        switch (event) {
            case TURN_START:
                Platform.runLater(() -> {
                    PlayerLabel pl = PlayerPane.getInstance().getPlayerLabel(data);
                    ;
                    Bounds b = pl.localToScene(pl.getBoundsInLocal());

                    focusPlayerOnTurnAnimation.setDimensions(PlayerPane.getInstance().getWidth(), b.getHeight() + 10);
                    focusPlayerOnTurnAnimation.setSceneCoordinates(0.0, b.getMinY() - 5);
                    focusPlayerOnTurnAnimation.play(gameRoot, 0).getValue().getStyleClass().addAll("fleeting",
                            "turn-start-animation");
                });
                try {
                    focusPlayerOnTurnAnimation.latch.await();
                } catch (InterruptedException e) {
                }
                focusPlayerOnTurnAnimation.resetLatch();
                break;
            case TURN_END:
                Platform.runLater(() -> {
                    ObservableList<Node> l = gameRoot.getChildren();
                    l.remove(l.indexOf(l.stream().filter(n -> n.getStyleClass().contains("turn-start-animation"))
                            .findFirst().get()));
                });
                break;
            case TURN_BLOCKED:
                Platform.runLater(() -> blockTurnAnimation.play(gameRoot));
                try {
                    blockTurnAnimation.latch.await();
                } catch (InterruptedException e) {
                }
                blockTurnAnimation.resetLatch();
                break;
            case PLAYER_WON:
                Platform.runLater(() -> {
                    Sounds.IN_GAME_SOUNDTRACK.stop();
                    gameThread.interrupt();
                    EndGameSettings.updateWinner(data.info().getIcon(), data.info().getNick());
                    changeRoot(endGameRoot);
                });
            default:
                throwUnsupportedError(event, data);
        }
    }

    @Override
    public void update(EventType event, int data) {
        switch (event) {
            case XP_EARNED:
                Platform.runLater(() -> EndGameSettings.updateUser(data, Info.userLevelProgress()));
                break;
            default:
                throwUnsupportedError(event, data);
        }
    }

    @Override
    public int getEventPriority(EventType event) {
        switch (event) {
            case CARD_CHANGE:
                return 2;
            default:
                return super.getEventPriority(event);
        }
    }

    /* APPLICATION METHODS */

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("JUno");
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.setOnCloseRequest(e -> System.exit(0));
        stage.setScene(scene);
        loadMedia();
        setupControllers();
        Loop.events.subscribe(this, getEventsListening().stream().toArray(EventType[]::new));
        stage.show();
    }

    /* MAIN */

    public static void main(String[] args) {
        PlayerData.getPlayerData("resources/Data/userInfo.txt"); // Used to load the user info
        launch(args);
    }

}
