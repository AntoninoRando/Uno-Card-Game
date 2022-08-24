import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

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
import model.gameLogic.Player;
import model.gameLogic.Card;
import model.data.UserInfo;

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

import controller.Controller;
import controller.ControllerFX;
import controller.Controls;

public class App extends Displayer {
    private Scene scene;

    private StackPane rootContainer = new StackPane();

    private StackPane homeRoot = newHomeRoot();
    private HomeMenu home;

    private StackPane gameRoot = newGameRoot();
    private StackPane gameElements;
    private Thread gameThread;

    private StackPane endGameRoot = newEndGameRoot();

    public App() {
        super("gameStart", "unoDeclared", "turnBlocked", "enemyTurn cardPlayed", "warning", "turnStart", "turnEnd",
                "playerWon", "reset", "gameSetupped");

        scene = new Scene(rootContainer, 1000, 600);
        changeRoot(homeRoot);

        scene.getStylesheets().add(getClass().getResource("resources\\Style.css").toExternalForm());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> UserInfo.writeData("resources\\data\\userInfo.txt")));
    }

    /* ROOTS */

    private void changeRoot(Pane newRoot) {
        rootContainer.getChildren().setAll(newRoot);
    }

    private StackPane newHomeRoot() {
        StackPane root = new StackPane();

        HomeMenu home = HomeMenu.getInstance();
        Homes.setProfileAction(e -> Settings.PROFILE.setVisible(true));
        Homes.setPlayButtonAction(e -> newGame());

        Settings.setNickFieldAction(e -> UserInfo.setNick(e));
        Settings.setDeleteAccountAction(e -> UserInfo.reset());
        Settings.setAvatarClickAction(e -> Settings.AVATAR_PICKER.setVisible(!Settings.AVATAR_PICKER.isVisible()));
        Settings.setCloseProfileOnClickOutside(home);

        root.getChildren().addAll(home, Settings.PROFILE, Settings.AVATAR_PICKER);
        StackPane.setAlignment(home, Pos.TOP_LEFT);

        Settings.PROFILE.setVisible(false);
        Settings.AVATAR_PICKER.setVisible(false);

        return root;
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
        playAgain.setOnMouseClicked(e -> newGame());
        playAgain.setStyle("-fx-background-color: none");

        ImageView icon = new ImageView(new Image("resources\\BlueButton.png"));
        icon.setPreserveRatio(true);
        icon.setFitWidth(150.0);
        playAgain.setGraphic(icon);

        Button backHome = new Button();
        backHome.setOnMouseClicked(e -> {
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

    private void loadAnimations() {
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
    }

    /* GAME INITIALIZATION */

    private void newGame() {
        Player p1 = new Player(UserInfo.getNick(), true);
        Player p2 = new Player("Top Princessess", false, "resources/icons/queen.png");
        Player p3 = new Player("Bot Luca", false, "resources/icons/blood.png");
        Player p4 = new Player("Bot Giovanni", false, "resources/icons/tree.png");

        Controller c1 = new ControllerFX();
        c1.setSource(p1);

        TreeMap<Integer, Player> players = new TreeMap<>();
        players.put(0, p1);
        players.put(1, p2);
        players.put(2, p3);
        players.put(3, p4);

        Loop.getInstance().setupGame(players, c1);

        Animation a1 = Animations.NEW_GAME.get();
        Animation a2 = Animations.NEW_GAME.get();
        Rectangle2D screenB = Screen.getPrimary().getBounds();
        a1.setDimensions(screenB.getWidth(), screenB.getHeight());
        a2.setDimensions(screenB.getWidth(), screenB.getHeight());

        a1.setStopFrame(5);
        a1.setWillStay(true);
        a1.setOnFinishAction(e -> {
            gameThread = new Thread(() -> Loop.getInstance().play());
            gameThread.start();

            rootContainer.getChildren().remove(0);
            rootContainer.getChildren().add(0, gameRoot);

            a2.play(rootContainer);
        });

        a2.setStartFrame(6);

        a1.play(rootContainer);
    }

    private void endGame() {
        Sounds.IN_GAME_SOUNDTRACK.stop();
        Loop.getInstance().endGame(true);
        gameThread.interrupt();
    }

    /* EVENT LISTENER METHODS */

    @Override
    public void update(String eventType, Object... data) {
        // TODO le animazioni le gioca toppo on the top: stanno sopra al menù di pausa
        switch (eventType) {
            case "gameStart":
                Platform.runLater(() -> {
                    Sounds.IN_GAME_SOUNDTRACK.play();
                    int i = 0;
                    while (i < gameRoot.getChildren().size()) {
                        if (gameRoot.getChildren().get(i).getStyleClass().contains("fleeting"))
                            gameRoot.getChildren().remove(i);
                        else
                            i += 1;
                    }
                });
                break;
            case "gameSetupped":
            case "enemyTurn cardPlayed":
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
            case "warning":
                Platform.runLater(() -> ResetTranslate.resetTranslate(((Card) data[1]).getGuiContainer()));
                break;
            case "unoDeclared":
                Platform.runLater(() -> {
                    unoAnimation.setSceneCoordinates(scene.getWidth() / 2 - 200, scene.getHeight() / 2 - 111.85);
                    unoAnimation.play(gameRoot);
                });
                break;
            case "turnBlocked":
                Platform.runLater(() -> blockTurnAnimation.play(gameRoot));
                try {
                    blockTurnAnimation.latch.await();
                } catch (InterruptedException e) {
                }
                blockTurnAnimation.resetLatch();
                break;
            case "turnStart":
                Platform.runLater(() -> {
                    PlayerLabel pl;
                    while (true) {
                        try {
                            pl = PlayerPane.getInstance().getPlayerLabel((Player) data[0]);
                            if (pl != null)
                                break;
                        } catch (NullPointerException e) {
                        }
                    }
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
            case "turnEnd":
                Platform.runLater(() -> {
                    ObservableList<Node> l = gameRoot.getChildren();
                    l.remove(l.indexOf(l.stream().filter(n -> n.getStyleClass().contains("turn-start-animation"))
                            .findFirst().get()));
                });
                break;
            case "playerWon":
                Platform.runLater(() -> {
                    Sounds.IN_GAME_SOUNDTRACK.stop();
                    gameThread.interrupt();

                    Player winner = (Player) data[0];
                    int xpEarned = (int) data[1];
                    String iconPath = winner.isHuman() ? UserInfo.getIconPath() : winner.getIconPath();
                    EndGameSettings.updateGameResults(iconPath, winner.getNickname(), xpEarned);

                    changeRoot(endGameRoot);
                });
                break;
        }
    }

    @Override
    public int getEventPriority(String eventLabel) {
        switch (eventLabel) {
            case "enemyTurn cardPlayed":
                return 2;
            default:
                return super.getEventPriority(eventLabel);
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
        loadAnimations();
        Loop.events.subscribe(this, getEventsListening().stream().toArray(String[]::new));
        stage.show();
    }

    /* MAIN */

    public static void main(String[] args) {
        UserInfo.loadData("resources\\data\\userInfo.txt");
        launch(args);
    }

}
