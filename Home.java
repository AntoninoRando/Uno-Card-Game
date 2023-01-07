import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import view.Visible;

/* --- Mine ------------------------------- */

import view.animations.Animation;
import view.animations.Animations;
import view.settings.ProfileMenu;
import view.sounds.Sounds;

public class Home extends StackPane implements AppState, Visible {
    /* --- Singleton -------------------------- */

    private static Home instance;

    public static Home getInstance() {
        if (instance == null)
            instance = new Home();
        return instance;
    }

    private Home() {
        createElements();
        arrangeElements();
    }

    /* --- Fields ----------------------------- */

    private JUno app; // context
    private Label title;
    private Button play;
    private Button profile;
    private Button exit;
    private ProfileMenu profileMenu;

    /* --- Body ------------------------------- */

    private void play() {
        Animation closing = Animations.NEW_GAME.get();
        closing.setStopFrame(5);
        closing.setDimensions(app.getScene().getWidth(), app.getScene().getHeight());

        closing.setOnFinishAction(e -> {
            InGame.getInstance().setContext(app);
            app.changeState(InGame.getInstance());
        });

        Sounds.BUTTON_CLICK.play();
        closing.play(this);
    }

    private void openProfile() {
        Sounds.BUTTON_CLICK.play();
        profileMenu.setVisible(!profileMenu.isVisible());
    }

    private void exit() {
        Sounds.BUTTON_CLICK.play();
        System.exit(0);
    }

    private void initializeProfileMenu() {
        profileMenu.setVisible(false);
        // profileMenu.onDelete(e -> PlayerData.reset());
        // profileMenu.onNickType(newNick -> PlayerData.setUserNick(newNick));
        // profileMenu.getAvatarPicker().onChoice(icoPath ->
        // PlayerData.setUserIcon(icoPath));
        // profileMenu.setCloseContainer(settingsContainer);
        // try {
        // profileMenu.getAvatarPicker().addOptions(Info.allIcons());
        // } catch (IOException e1) {
        // }

        // Info.events.subscribe(profileMenu, Event.USER_NEW_NICK, Event.USER_NEW_ICON,
        // Event.NEW_LEVEL_PROGRESS,
        // Event.USER_PLAYED_GAME, Event.USER_WON, Event.LEVELED_UP, Event.INFO_RESET);

        // Setting the first values
        // profileMenu.update(EventType.USER_NEW_NICK, PlayerData.getUserNick());
        // profileMenu.update(EventType.USER_NEW_ICON, PlayerData.getUserIcon());
        // profileMenu.update(EventType.LEVELED_UP, PlayerData.getLevel());
        // profileMenu.update(EventType.USER_PLAYED_GAME, PlayerData.getGames());
        // profileMenu.update(EventType.USER_WON, (int) PlayerData.getWins());
        // profileMenu.update(EventType.NEW_LEVEL_PROGRESS, Info.userLevelProgress());
    }

    /* --- Visible ---------------------------- */

    @Override
    public void createElements() {
        title = new Label("JUno");
        play = new Button("Play");
        profile = new Button("Profile");
        exit = new Button("Exit");
        profileMenu = ProfileMenu.getInstance();

        initializeProfileMenu();

        play.setOnMouseClicked(e -> play());
        profile.setOnMouseClicked(e -> openProfile());
        exit.setOnMouseClicked(e -> exit());

        title.getStyleClass().add("title");
        play.getStyleClass().add("button");
        profile.getStyleClass().add("button");
        exit.getStyleClass().add("button");

    }

    @Override
    public void arrangeElements() {
        VBox buttons = new VBox(title, play, profile, exit);
        buttons.getStyleClass().add("home-menu");
        buttons.setSpacing(10.0);
        buttons.setAlignment(Pos.CENTER_LEFT);

        getChildren().addAll(buttons, profileMenu);
        StackPane.setAlignment(buttons, Pos.TOP_LEFT);
    }
    
    /* --- State ------------------------------ */

    public void setContext(JUno app) {
        this.app = app;
    }


    @Override
    public void display() {
    }
}
