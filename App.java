import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.JUno;
import model.Loop;

import view.DeckContainer;
import view.Displayer;
import view.EnemyPane;
import view.HandPane;
import view.PlayzonePane;
import view.SelectionPane;
import view.TerrainPane;
import view.animations.AnimationLayer;
import view.animations.Animations;
import view.home.HomeMenu;
import view.home.Homes;
import view.settings.Settings;
import view.sounds.Sounds;

public class App extends Displayer {
    private Scene scene;
    private StackPane root;
    private StackPane gameElements;

    public App() {
        super("gameStart", "unoDeclared", "turnBlocked", "reset");

        root = new StackPane();
        root.setId("background");

        scene = new Scene(root, 1000, 600);
        scene.getStylesheets().add(getClass().getResource("resources\\Style.css").toExternalForm());
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
        borderPane.setRight(DeckContainer.getInstance());
        borderPane.setCenter(TerrainPane.getInstance());
        borderPane.setBottom(HandPane.getInstance());
        
        gameElements.getChildren().addAll(borderPane, AnimationLayer.getInstance(), PlayzonePane.getInstance(),
        SelectionPane.getInstance());
        
        root.getChildren().add(gameElements);
    }

    private void addHomePageContents() {
        HomeMenu home = HomeMenu.getInstance();

        root.getChildren().add(home);
        StackPane.setAlignment(home, Pos.TOP_LEFT);

        Homes.setPlayButtonAction(e -> newGame());
    }

    private void addSettingsContents() {
        root.getChildren().addAll(Settings.MENU, Settings.BUTTON);
        StackPane.setAlignment(Settings.BUTTON, Pos.TOP_RIGHT);
    }
    
    private void loadAnimations() {
        Animations.UNO_TEXT.get().load();
        Animations.BLOCK_TURN.get().load();
        Animations.CARD_PLAYED.get().load();
        Animations.FOCUS_PLAYER.get().load();
    }

    private void newGame() {
        Loop.getInstance().setupView(this);
        JUno.getInstance().start();
        gameElements.setVisible(true);
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
            case "gameStart":
                Platform.runLater(() -> Sounds.IN_GAME_SOUNDTRACK.play());
                break;
            case "unoDeclared":
                Platform.runLater(() -> Animations.UNO_TEXT.get().play(AnimationLayer.getInstance()));
                break;
            case "turnBlocked":
                Platform.runLater(() -> Animations.BLOCK_TURN.get().playOnQueue(AnimationLayer.getInstance()));
                break;
            case "reset":
                // TODO
                break;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
