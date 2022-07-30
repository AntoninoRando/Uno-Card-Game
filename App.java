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
import view.settings.Settings;
import view.sounds.Sounds;

public class App extends Displayer {
    private StackPane root;
    private Scene scene;

    public App() {
        super("gameStart", "unoDeclared", "turnBlocked", "reset");

        root = new StackPane();
        root.setId("background");

        scene = new Scene(root, 1000, 600);
        scene.getStylesheets().add(getClass().getResource("view\\Style.css").toExternalForm());
    }


    private void addGameContents() {
        BorderPane gameElements = new BorderPane();
        gameElements.setLeft(EnemyPane.getInstance());
        gameElements.setRight(DeckContainer.getInstance());
        gameElements.setCenter(TerrainPane.getInstance());
        gameElements.setBottom(HandPane.getInstance());

        root.getChildren().add(gameElements);
        root.getChildren().add(AnimationLayer.getInstance());
        root.getChildren().add(PlayzonePane.getInstance());
        root.getChildren().add(SelectionPane.getInstance());
        root.getChildren().add(Settings.MENU);
        root.getChildren().add(Settings.BUTTON);
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
    }

    /* ---------------------------------------------------- */

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("JUno");

        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");

        // TODO Non so se sia corretto...
        stage.setOnCloseRequest(e -> System.exit(0));

        addGameContents();
        stage.setScene(scene);
        loadAnimations();

        stage.show();
        newGame();
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
