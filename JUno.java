import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import controller.Controls;
import controller.DropAndPlay;
import events.EventListener;
import events.EventManager;
import events.Event;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.gameLogic.Game;
import model.GameThread;
import model.data.Info;
import model.data.PlayerData;
import model.gameEntities.Enemies;
import model.gameEntities.Player;
import view.CUView;
import view.GameResults;
import view.animations.Animation;
import view.animations.Animations;
import view.animations.ResetTranslate;
import view.gameElements.PlayerPane;
import view.gameElements.Card;
import view.gameElements.CardChronology;
import view.gameElements.HandPane;
import view.gameElements.PlayerLabel;
import view.gameElements.PlayzonePane;
import view.gameElements.SelectionPane;
import view.gameElements.TerrainPane;
import view.settings.ProfileMenu;
import view.settings.SettingsMenu;
import view.sounds.Sounds;

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

    /* --- Body ------------------------------- */

    private Animation unoAnimation;
    private Animation blockTurnAnimation;
    private Animation playingCardAnimation;
    private Animation focusPlayerOnTurnAnimation;

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

        Sounds.loadAll();
    }

    /* --- State ------------------------------ */

    private AppState state;

    public void changeState(AppState state) {
        this.state = state;
        root.getChildren().remove(0);
        root.getChildren().add(0, (Pane) state);
        state.display();
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

        // TODO spostare nell'InGame.java
        // Button restart = new Button("Restart");
        // Button quit = new Button("Quit game");

        // restart.setOnMouseClicked(e -> {
        // Sounds.BUTTON_CLICK.play();
        // settings.setVisible(false);
        // // quitGame();
        // // newGame();
        // });
        // quit.setOnMouseClicked(e -> {
        // Sounds.BUTTON_CLICK.play();
        // settings.setVisible(false);
        // // quitGame();
        // // changeRoot(homeRoot);
        // });
    }

    private void arrangeElements() {
        settings.setVisible(false);
        root.getChildren().addAll(Home.getInstance(), settings, settingsButton);
        StackPane.setAlignment(settingsButton, Pos.TOP_RIGHT);

        Home.getInstance().setContext(this);
    }

    /* --- Observer --------------------------- */

    // Modificare e fare che legge un Json

    private void subscribeAll() {
        CUView.getInstance().subscribe(this, Event.GAME_READY, Event.GAME_START, Event.CARD_CHANGE, Event.UNO_DECLARED,
                Event.TURN_BLOCKED, Event.TURN_END, Event.INVALID_CARD, Event.TURN_START,
                Event.PLAYER_WON);
    }

    @Override
    public void update(Event event, HashMap<String, Object> data) {
        // switch (event) {
        //     case GAME_READY:
        //         Platform.runLater(() -> {
        //             int i = 0;
        //             while (i < gameRoot.getChildren().size()) {
        //                 if (gameRoot.getChildren().get(i).getStyleClass().contains("fleeting"))
        //                     gameRoot.getChildren().remove(i);
        //                 else
        //                     i += 1;
        //             }
        //         });
        //         break;
        //     case GAME_START:
        //         Platform.runLater(() -> {
        //             Sounds.IN_GAME_SOUNDTRACK.play();
        //             a2.play(rootContainer);
        //         });
        //         break;
        //     // About players
        //     case TURN_START:
        //         Platform.runLater(() -> {
        //             String nick = (String) data.get("nickname");
        //             PlayerLabel pl = PlayerPane.getInstance().getPlayerLabel(nick);
        //             ;
        //             Bounds b = pl.localToScene(pl.getBoundsInLocal());

        //             focusPlayerOnTurnAnimation.setDimensions(PlayerPane.getInstance().getWidth(), b.getHeight() + 10);
        //             focusPlayerOnTurnAnimation.setSceneCoordinates(0.0, b.getMinY() - 5);
        //             focusPlayerOnTurnAnimation.play(gameRoot, 0).getValue().getStyleClass().addAll("fleeting",
        //                     "turn-start-animation");
        //         });
        //         try {
        //             focusPlayerOnTurnAnimation.latch.await();
        //         } catch (InterruptedException e) {
        //         }
        //         focusPlayerOnTurnAnimation.resetLatch();
        //         break;
        //     case TURN_END:
        //         Platform.runLater(() -> {
        //             ObservableList<Node> l = gameRoot.getChildren();
        //             l.remove(l.indexOf(l.stream().filter(n -> n.getStyleClass().contains("turn-start-animation"))
        //                     .findFirst().get()));
        //         });
        //         break;
        //     case TURN_BLOCKED:
        //         Platform.runLater(() -> blockTurnAnimation.play(gameRoot));
        //         try {
        //             blockTurnAnimation.latch.await();
        //         } catch (InterruptedException e) {
        //         }
        //         blockTurnAnimation.resetLatch();
        //         break;
        //     case PLAYER_WON:
        //         Platform.runLater(() -> {
        //             endGame();
        //         });
        //         break;
        //     case CARD_CHANGE:
        //         Platform.runLater(() -> {
        //             playingCardAnimation.setSceneCoordinates(scene.getWidth() / 2, scene.getHeight() / 2);
        //             playingCardAnimation.play(gameRoot, gameRoot.getChildren().size() - 2);
        //         });
        //         try {
        //             playingCardAnimation.latch.await();
        //         } catch (InterruptedException e) {
        //         }
        //         playingCardAnimation.resetLatch();
        //         break;
        //     case INVALID_CARD:
        //         Platform.runLater(() -> ResetTranslate.resetTranslate(Card.cards.get((int) data.get("card-tag"))));
        //         break;
        //     case UNO_DECLARED:
        //         Platform.runLater(() -> {
        //             unoAnimation.setSceneCoordinates(scene.getWidth() / 2 - 200, scene.getHeight() / 2 - 111.85);
        //             unoAnimation.play(gameRoot);
        //         });
        //         break;
        //     default:
        //         throwUnsupportedError(event, null);
        // }
    }

    @Override
    public int getEventPriority(Event event) {
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
        subscribeAll();
        stage.show();
    }

    public static void main(String[] args) {
        PlayerData.getPlayerData("resources\\Data\\userInfo.txt"); // Used to load the user info
        launch(args);
    }

}
