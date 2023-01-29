import java.net.MalformedURLException;
import java.nio.file.Paths;

import javafx.animation.RotateTransition;
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
import javafx.util.Duration;

/* --- Mine ------------------------------- */

import model.data.UserData;
import view.GUIContainer;
import view.animations.Animations;
import view.settings.SettingsMenu;
import view.sounds.Sounds;

import events.EventListener;

public class JUno extends Application implements EventListener, GUIContainer {
    /* --- Fields ----------------------------- */

    private AppState state;
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
        applyBehaviors();
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

    public static void main(String[] args) {
        String userDataPath = "resources\\Data\\userInfo.txt";
        UserData.load(userDataPath);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> UserData.write(userDataPath)));
        launch(args);
    }

    /* --- Visible ---------------------------- */

    @Override
    public void createElements() {
        root = new StackPane();
        scene = new Scene(root, 1000, 600);
        settings = SettingsMenu.getInstance();
        settingsButton = new Button();

        settingsButton.setId("settings-button");

        try {
            Image image = new Image(Paths.get("resources\\gear.png").toUri().toURL().toExternalForm());
            ImageView icon = new ImageView(image);
            icon.setPreserveRatio(true);
            icon.setFitWidth(70.0);
            settingsButton.setGraphic(icon);
            settingsButton.setOnMouseEntered(e -> {
                Sounds.GEAR.play();

                RotateTransition transition = new RotateTransition();
                transition.setCycleCount(1);
                transition.setNode(settingsButton);
                transition.setDuration(Duration.seconds(1));
                transition.setFromAngle(0);
                transition.setToAngle(360);
                transition.play();
            });
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void arrangeElements() {
        scene.getStylesheets().add(getClass().getResource("resources\\Style.css").toExternalForm());
        settings.setVisible(false);
        root.getChildren().addAll(Home.getInstance(), settings, settingsButton);
        StackPane.setAlignment(settingsButton, Pos.TOP_RIGHT);

        Home.getInstance().setContext(this);
    }

    @Override
    public void applyBehaviors() {
        settingsButton.setOnMouseClicked(e -> {
            Sounds.BUTTON_CLICK.play();
            settings.setVisible(!settings.isVisible());
        });
    }

    /* --- State ------------------------------ */

    public void changeState(AppState arrivalState) {
        this.state = arrivalState;
        Platform.runLater(() -> {
            root.getChildren().remove(0);
            root.getChildren().add(0, (Pane) state);
            state.display();
        });
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
}
