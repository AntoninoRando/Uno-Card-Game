import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Stream;

import controller.DragAndDrop;
import controller.DeclareUno;
import controller.Draw;
import controller.Select;
import events.toView.EventListener;
import events.toView.EventManager;
import events.toView.EventType;
import javafx.application.Application;
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

import prefabs.Card;
import prefabs.Player;

import model.gameLogic.Loop;
import model.GameThread;
import model.data.Info;
import model.data.PlayerData;

import view.GameResults;
import view.HomeMenu;
import view.animations.Animation;
import view.animations.Animations;
import view.animations.ResetTranslate;
import view.gameElements.PlayerPane;
import view.gameElements.Chronology;
import view.gameElements.HandPane;
import view.gameElements.PlayerLabel;
import view.gameElements.PlayzonePane;
import view.gameElements.SelectionPane;
import view.gameElements.TerrainPane;
import view.settings.ProfileMenu;
import view.settings.SettingsMenu;
import view.sounds.Sounds;

public class JUno extends Application implements EventListener {
    private Scene scene;
    private StackPane rootContainer = new StackPane();
    private StackPane homeRoot;
    private StackPane gameRoot;
    private StackPane endGameRoot;

    public JUno() {
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
        home.setPlayButtonAction(e -> {
            Sounds.BUTTON_CLICK.play();
            newGame();
        });
        home.setProfileAction(e -> {
            Sounds.BUTTON_CLICK.play();
            ProfileMenu.getInstance().setVisible(!ProfileMenu.getInstance().isVisible());
        });

        root.getChildren().addAll(home, ProfileMenu.getInstance());
        StackPane.setAlignment(home, Pos.TOP_LEFT);

        initializeProfile(home);

        return root;
    }

    private void initializeProfile(Pane settingsContainer) {
        ProfileMenu pm = ProfileMenu.getInstance();
        pm.setVisible(false);
        pm.onDelete(e -> PlayerData.reset());
        pm.onNickType(newNick -> PlayerData.setUserNick(newNick));
        pm.getAvatarPicker().onChoice(icoPath -> PlayerData.setUserIcon(icoPath));
        pm.setCloseContainer(settingsContainer);
        try {
            pm.getAvatarPicker().addOptions(Info.allIcons());
        } catch (IOException e1) {
        }

        Info.events.subscribe(pm, EventType.USER_NEW_NICK, EventType.USER_NEW_ICON, EventType.NEW_LEVEL_PROGRESS,
                EventType.USER_PLAYED_GAME, EventType.USER_WON, EventType.LEVELED_UP, EventType.INFO_RESET);

        // Setting the first values
        pm.update(EventType.USER_NEW_NICK, PlayerData.getUserNick());
        pm.update(EventType.USER_NEW_ICON, PlayerData.getUserIcon());
        pm.update(EventType.LEVELED_UP, PlayerData.getLevel());
        pm.update(EventType.USER_PLAYED_GAME, PlayerData.getGames());
        pm.update(EventType.USER_WON, (int) PlayerData.getWins());
        pm.update(EventType.NEW_LEVEL_PROGRESS, Info.userLevelProgress());
    }

    private Timer scrollTimer;

    private StackPane newGameRoot() {
        StackPane root = new StackPane();
        root.setId("background");

        BorderPane gameElements = new BorderPane();
        gameElements.setLeft(PlayerPane.getInstance());

        StackPane center = new StackPane(TerrainPane.getInstance(), Chronology.getInstance());
        Chronology.getInstance().setVisible(false);
        gameElements.setCenter(center);

        gameElements.setBottom(HandPane.getInstance()); // Added after the terrain card, so that the hand's cards are
                                                        // displayed on top

        Region padderRegionRight = new Region();
        padderRegionRight.prefWidthProperty().bind(PlayerPane.getInstance().widthProperty());
        gameElements.setRight(padderRegionRight);

        root.getChildren().addAll(gameElements, PlayzonePane.getInstance(), SelectionPane.getInstance());

        PlayzonePane.getInstance().setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.SECONDARY))
                DeclareUno.getInstance().fire();
            else
                Draw.getInstance().fire();
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
        // End Game Buttons
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
        });
        backHome.setStyle("-fx-background-color: none");
        ImageView icon2 = new ImageView(new Image("resources\\HomeButton.png"));
        icon2.setPreserveRatio(true);
        icon2.setFitWidth(150.0);
        backHome.setGraphic(icon2);

        GameResults.getInstance().addButtons(playAgain, backHome);

        return new StackPane(GameResults.getInstance());
    }

    private void addSettingsContents(Pane root) {
        SettingsMenu sm = SettingsMenu.getInstance();
        sm.setVisible(false);

        Button restartB = new Button("Restart");
        Button quitB = new Button("Quit game");
        restartB.setOnMouseClicked(e -> {
            Sounds.BUTTON_CLICK.play();
            sm.setVisible(false);
            quitGame();
            newGame();
        });
        quitB.setOnMouseClicked(e -> {
            Sounds.BUTTON_CLICK.play();
            sm.setVisible(false);
            quitGame();
            changeRoot(homeRoot);
        });
        sm.inGameMenu(restartB, quitB);

        Button openB = new Button();
        openB.setId("settings-button");
        try {
            Image image = new Image(Paths.get("resources\\settingsIcon.png").toUri().toURL().toExternalForm());
            ImageView icon = new ImageView(image);
            icon.setFitWidth(50.0);
            icon.setFitHeight(50.0);
            openB.setGraphic(icon);
        } catch (MalformedURLException e1) {
        }
        openB.setOnMouseClicked(__ -> sm.setVisible(!sm.isVisible()));
        root.getChildren().addAll(sm, openB);
        StackPane.setAlignment(openB, Pos.TOP_RIGHT);
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
            GameThread.play();

            rootContainer.getChildren().remove(0);
            rootContainer.getChildren().add(0, gameRoot);
        });

        a2.setStartFrame(6);

        Sounds.loadAll();
    }

    // Start and end a new game

    private void newGame() {
        Player[] players = Stream.of("userInfo", "BotTopPrincessess", "BotLuca", "BotGiorgio")
                .map(s -> new Player("resources\\Data\\" + s + ".txt"))
                .sorted((a, b) -> new Random().nextBoolean() ? 1 : -1)
                .toArray(Player[]::new);

        Loop.getInstance().setupGame(players);

        Rectangle2D screenB = Screen.getPrimary().getBounds();
        a1.setDimensions(screenB.getWidth(), screenB.getHeight());
        a2.setDimensions(screenB.getWidth(), screenB.getHeight());
        a1.play(rootContainer);
    }

    private void endGame() {
        GameThread.stop(false);
        Sounds.IN_GAME_SOUNDTRACK.stop();
        changeRoot(endGameRoot);
    }

    private void quitGame() {
        GameThread.stop(true);
        Sounds.IN_GAME_SOUNDTRACK.stop();
    }

    /* EVENT LISTENER METHODS */

    // TODO le animazioni le gioca toppo on the top: stanno sopra al menù di pausa

    private void subscribeEventListeners() {
        EventManager em = Loop.events;
        em.subscribe(this, EventType.GAME_READY, EventType.GAME_START, EventType.CARD_CHANGE, EventType.UNO_DECLARED,
                EventType.TURN_BLOCKED, EventType.USER_DREW,
                EventType.TURN_END, EventType.INVALID_CARD, EventType.TURN_START, EventType.PLAYER_WON);
        em.subscribe(GameResults.getInstance(), EventType.PLAYER_WON);
        Info.events.subscribe(GameResults.getInstance(), EventType.XP_EARNED, EventType.NEW_LEVEL_PROGRESS);
        em.subscribe(SettingsMenu.getInstance(), EventType.GAME_START, EventType.RESET);
        em.subscribe(Chronology.getInstance(), EventType.CARD_CHANGE, EventType.TURN_BLOCKED, EventType.PLAYER_DREW,
                EventType.PLAYER_PLAYED_CARD, EventType.RESET);
        em.subscribe(HandPane.getInstance(), EventType.GAME_READY, EventType.USER_PLAYED_CARD, EventType.USER_DREW);
        em.subscribe(PlayerPane.getInstance(), EventType.GAME_READY, EventType.PLAYER_HAND_DECREASE,
                EventType.PLAYER_HAND_INCREASE);
        em.subscribe(SelectionPane.getInstance(), EventType.USER_SELECTING_CARD);
        em.subscribe(TerrainPane.getInstance(), EventType.GAME_READY, EventType.CARD_CHANGE);
    }

    private void subscribeInputListeners() {
        Loop il = Loop.getInstance();
        DragAndDrop.getInstance().setDropTarget(PlayzonePane.getInstance());
        DragAndDrop.getInstance().setListener(il);
        DeclareUno.getInstance().setListener(il);
        Draw.getInstance().setListener(il);
        Select.setGlobalListener(il);
    }

    private void subscribeViewListeners() {
        // TODO
    }

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
                subscribeInputListeners();
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
            case USER_DREW:
                DragAndDrop.getInstance().setControls(data);
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
                    endGame();
                });
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
                return 1;
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("JUno");
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.setOnCloseRequest(e -> System.exit(0));
        stage.setScene(scene);
        loadMedia();
        subscribeEventListeners();
        subscribeInputListeners();
        stage.show();
    }

    public static void main(String[] args) {
        PlayerData.getPlayerData("resources\\Data\\userInfo.txt"); // Used to load the user info
        launch(args);
    }

}
