import java.net.MalformedURLException;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/* --- Mine ------------------------------- */

import model.data.PlayerData;

import view.animations.Animations;
import view.settings.SettingsMenu;
import view.sounds.Sounds;

import events.EventListener;

public class JUno extends Application implements EventListener {
    /* --- Fields ----------------------------- */

    private Scene scene;
    private StackPane root;
    private SettingsMenu settings;
    private Button settingsButton;

    /* ---.--- Getters and Setters ------------ */

    public Scene getScene() {
        return scene;
    }

    /* --- Constructors ----------------------- */

    public JUno() {
        createElements();
        arrangeElements();

        scene.getStylesheets().add(getClass().getResource("resources\\Style.css").toExternalForm());
    }

    /* --- Application ------------------------ */

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("JUno");
        stage.setFullScreen(true);
        stage.setFullScreenExitHint("");
        stage.setOnCloseRequest(e -> System.exit(0));
        stage.setScene(scene);
        loadMedia();
        stage.show();
    }

    public static void main(String[] args) {
        PlayerData.getPlayerData("resources\\Data\\userInfo.txt"); // Used to load the user info
        launch(args);
    }

    /* --- Body ------------------------------- */

    private void loadMedia() {
        Animations.FOCUS_PLAYER.get().load();
        Animations.NEW_GAME.get().load();
        Animations.UNO_TEXT.get().load();
        Animations.BLOCK_TURN.get().load();
        Animations.CARD_PLAYED.get().load();

        Sounds.loadAll();
    }

    /* --- State ------------------------------ */

    private AppState state;

    public void changeState(AppState arrivalState) {
        this.state = arrivalState;
        Platform.runLater(() -> {
            root.getChildren().remove(0);
            root.getChildren().add(0, (Pane) state);
            state.display();
        });
    }

    private void createElements() {
        root = new StackPane();
        scene = new Scene(root, 1000, 600);
        settings = SettingsMenu.getInstance();
        settingsButton = new Button();

        settingsButton.setOnMouseClicked(e -> {
            Sounds.BUTTON_CLICK.play();
            settings.setVisible(!settings.isVisible());
        });

        settingsButton.setId("settings-button");

        try {
            Image image = new Image(Paths.get("resources\\settingsIcon.png").toUri().toURL().toExternalForm());
            ImageView icon = new ImageView(image);
            icon.setFitWidth(50.0);
            icon.setFitHeight(50.0);
            settingsButton.setGraphic(icon);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
    }

    private void arrangeElements() {
        settings.setVisible(false);
        root.getChildren().addAll(Home.getInstance(), settings, settingsButton);
        StackPane.setAlignment(settingsButton, Pos.TOP_RIGHT);

        Home.getInstance().setContext(this);
    }
}
