package view;

import model.Player;
import model.JUno;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameFX extends Displayer {
    /* SINGLETON */
    /* --------- */
    private StackPane root;
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
        root = createContent();
    }

    /* ---------------------------------------- */

    private StackPane createContent() {
        return new StackPane(new Text("Hello!"));
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(root, 1380, 760));
        stage.show();
    }

    public static void main(String[] args) {
        new JUno().start();
        launch(args);
    }

    @Override
    public void update(String eventType, Object data) {
        if (eventType.equals("gameStart"))
            Platform.runLater(() -> {
                Player player = (Player) data;
                int i = 0;
                while (i < player.getHand().size()) {
                    root.getChildren().add(new CardContainer(player.getHand().get(i)).getNode());
                    i++;
                }
            });
    }
}
