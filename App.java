import java.util.Collection;

import javafx.application.Platform;
import javafx.scene.Scene;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.JUno;
import model.Loop;
import model.Player;

import view.DeckContainer;
import view.Displayer;
import view.EnemyPane;
import view.HandPane;
import view.PlayzonePane;
import view.TerrainPane;
import view.animations.AnimationLayer;
import view.animations.Animations;
import view.sounds.Sounds;

public class App extends Displayer {
    /* SINGLETON */
    /* --------- */
    private static App instance;

    public static App getInstance() {
        if (instance == null)
            instance = new App();
        return instance;
    }

    public App() {
        super("gameStart", "unoDeclared", "turnBlocked");
        if (instance == null)
            instance = this;
    }

    /* ---------------------------------------- */

    private Scene addContent() {
        StackPane root = new StackPane();
        root.setId("background");
        Scene scene = new Scene(root, 1000, 600);
        scene.getStylesheets().add(getClass().getResource("view\\Style.css").toExternalForm());

        BorderPane gameElements = new BorderPane();
        gameElements.setLeft(EnemyPane.getInstance());
        gameElements.setRight(DeckContainer.getInstance());
        gameElements.setCenter(TerrainPane.getInstance());
        gameElements.setBottom(HandPane.getInstance());

        root.getChildren().add(gameElements);
        root.getChildren().add(AnimationLayer.getInstance());
        root.getChildren().add(PlayzonePane.getInstance());

        Animations.UNO_TEXT.load();
        Animations.BLOCK_TURN.load();
        Animations.CARD_PLAYED.load();

        return scene;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("JUno");

        // stage.setFullScreen(true);
        // stage.setFullScreenExitHint("");

        // TODO Non so se sia corretto...
        stage.setOnCloseRequest(e -> System.exit(0));

        stage.setScene(addContent());

        stage.show();
        Loop.getInstance().setupView(this);
        JUno.getInstance().start();
    }

    @Override
    public void update(String eventType, Object data) {
        if (eventType.equals("gameStart"))
            Platform.runLater(() -> {
                @SuppressWarnings("unchecked") // TODO non penso si debba fare
                Collection<Player> players = (Collection<Player>) data;
                for (Player player : players) {
                    if (!player.isHuman()) {
                        EnemyPane.getInstance().addEnemy(player);
                    }
                }

                Sounds.IN_GAME_SOUNDTRACK.play();
            });
        else if (eventType.equals("unoDeclared"))
            Platform.runLater(() -> Animations.UNO_TEXT.play());
        else if (eventType.equals("turnBlocked"))
            Platform.runLater(() -> Animations.BLOCK_TURN.play());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
