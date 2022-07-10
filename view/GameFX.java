package view;

import model.Player;
import model.JUno;

import java.util.Collection;

import controller.DragControl;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameFX extends Displayer {
    /* SINGLETON */
    /* --------- */
    private static GameFX instance;

    public static GameFX getInstance() {
        if (instance == null)
            instance = new GameFX();
        return instance;
    }

    public GameFX() {
        super("gameStart");
        if (instance == null)
            instance = this;
    }

    /* ---------------------------------------- */
    private BorderPane background;
    private HandPane hand;
    private PlayzonePane playzone;
    private EnemyPane enemies;

    private Pane createContent() {
        background = new BorderPane();

        enemies = new EnemyPane();
        background.setLeft(enemies);

        // TODO non ho capito come funziona tutto cio... in logica doveva far fittare
        // l'handPane nel background
        AnchorPane anchorPane = new AnchorPane();
        background.setBottom(anchorPane);
        hand = new HandPane();
        anchorPane.getChildren().add(hand);
        AnchorPane.setTopAnchor(hand, 0.0);
        AnchorPane.setBottomAnchor(hand, 0.0);
        AnchorPane.setLeftAnchor(hand, 0.0);
        AnchorPane.setRightAnchor(hand, 0.0);

        background.setRight(DeckContainer.getInstance());

        playzone = new PlayzonePane();
        background.setCenter(playzone);

        return background;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(createContent(), 1380, 760);
        scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

        DragControl.setDontResetZone(playzone.localToScene(playzone.getBoundsInLocal()));
        new JUno().start();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void update(String eventType, Object data) {
        if (eventType.equals("gameStart"))
            Platform.runLater(() -> {
                @SuppressWarnings("unchecked") // TODO non penso si debba fare
                Collection<Player> players = (Collection<Player>) data;
                for (Player player : players) {
                    if (!player.isHuman()) {
                        enemies.addEnemy(player);
                    }
                }
            });
    }
}
