import java.util.TreeMap;

import controller.Controller;
import controller.ControllerFX;
import controller.Controls;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.gameLogic.Loop;
import model.data.UserInfo;
import model.gameLogic.Player;
import model.gameLogic.Card;
import view.Displayer;
import view.animations.Animation;
import view.animations.Animations;
import view.animations.ResetTranslate;
import view.gameElements.EnemyPane;
import view.gameElements.HandPane;
import view.gameElements.PlayzonePane;
import view.gameElements.SelectionPane;
import view.gameElements.TerrainPane;
import view.home.HomeMenu;
import view.home.Homes;
import view.settings.Settings;
import view.sounds.Sounds;

public class App extends Displayer {
    private Scene scene;
    private StackPane root;
    private HomeMenu home;
    private StackPane gameElements;
    private Thread gameThread;

    public App() {
        super("gameStart", "unoDeclared", "turnBlocked", "enemyTurn cardPlayed", "warning");

        root = new StackPane();
        root.setId("background");

        scene = new Scene(root, 1000, 600);
        scene.getStylesheets().add(getClass().getResource("resources\\Style.css").toExternalForm());

        UserInfo.loadData("resources\\data\\userInfo.txt");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> UserInfo.writeData("resources\\data\\userInfo.txt")));
    }

    private void arrangeLayers() {
        addGameContents();
        addHomePageContents();
        addSettingsContents();
    }

    private void addGameContents() {
        gameElements = new StackPane();
        gameElements.setVisible(false);

        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(EnemyPane.getInstance());
        borderPane.setCenter(TerrainPane.getInstance());
        borderPane.setBottom(HandPane.getInstance());

        // Add padding to the borderPane right to absolute center the node in the
        // borderPane center region
        Region padderRegionRight = new Region();
        padderRegionRight.prefWidthProperty().bind(EnemyPane.getInstance().widthProperty());
        borderPane.setRight(padderRegionRight);

        gameElements.getChildren().addAll(borderPane, PlayzonePane.getInstance(),
                SelectionPane.getInstance());

        PlayzonePane.getInstance().setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.SECONDARY))
                Controls.DECLARE_UNO.getAction().handle(e);
            else
                Controls.DRAW.getAction().handle(e);
        });

        root.getChildren().add(gameElements);
    }

    private void addHomePageContents() {
        home = HomeMenu.getInstance();

        root.getChildren().add(home);
        StackPane.setAlignment(home, Pos.TOP_LEFT);

        Homes.setPlayButtonAction(e -> newGame());
    }

    private void addSettingsContents() {
        root.getChildren().addAll(Settings.MENU, Settings.SETTINGS_BUTTON);
        StackPane.setAlignment(Settings.SETTINGS_BUTTON, Pos.TOP_RIGHT);

        Settings.setRestartButtonAction(e -> {
            endGame();
            newGame();
        });
        Settings.setQuitButtonAction(e -> {
            endGame();
            gameElements.setVisible(false);
            home.setVisible(true);
        });
        Settings.setNickFieldAction(__ -> UserInfo.setNick(__));
        Settings.setDeleteAccountAction(e -> UserInfo.reset());
        Settings.setAvatarClickAction(e -> Settings.openAvatarPicker());
    }

    private void loadAnimations() {
        Animations.UNO_TEXT.get().load();
        Animations.BLOCK_TURN.get().load();
        Animations.CARD_PLAYED.get().load();
    }

    private void newGame() {
        Player p1 = new Player(UserInfo.getNick(), true);
        Player p2 = new Player("Top Princessess", false);
        Player p3 = new Player("Bot Luca", false);
        Player p4 = new Player("Bot Giovanni", false);

        Controller c1 = new ControllerFX();
        c1.setSource(p1);

        TreeMap<Integer, Player> players = new TreeMap<>();
        players.put(0, p1);
        players.put(1, p2);
        players.put(2, p3);
        players.put(3, p4);

        Loop.getInstance().setupGame(players, c1);
        Loop.getInstance().setupView(this);

        gameElements.setVisible(true);
        home.setVisible(false);

        gameThread = new Thread(() -> Loop.getInstance().play());
        gameThread.start();
    }

    private void endGame() {
        Sounds.IN_GAME_SOUNDTRACK.stop();
        Loop.reset();
        gameThread.interrupt();
    }

    /* ---------------------------------------------------- */

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("JUno");

        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");

        stage.setOnCloseRequest(e -> System.exit(0));

        arrangeLayers();
        loadAnimations();

        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void update(String eventType, Object data) {
        switch (eventType) {
            case "unoDeclared":
                Platform.runLater(() -> Animations.UNO_TEXT.get().play(root));
                break;
            case "turnBlocked":
                Platform.runLater(() -> Animations.BLOCK_TURN.get().playAndWait(root));
                try {
                    Animation.latch.await();
                } catch (InterruptedException e) {
                }
                break;
            case "enemyTurn cardPlayed":
                // TODO Aggiungere priorità all'animazione altrimenti parte dopo che è avvenuta la modifica
                Platform.runLater(() -> Animations.CARD_PLAYED.get().playAndWait(root));
                try {
                    Animation.latch.await();
                } catch (InterruptedException e) {
                }
                break;
        }
    }

    @Override
    public void update(String eventType, Object... data) {
        switch (eventType) {
            case "gameStart":
                Platform.runLater(() -> Sounds.IN_GAME_SOUNDTRACK.play());
                break;
            case "warning":
                Platform.runLater(() -> ResetTranslate.resetTranslate(((Card) data[1]).getGuiContainer()));
                break;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
