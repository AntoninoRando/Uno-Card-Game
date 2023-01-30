package view.media;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public enum Sound {
    BUTTON_CLICK("mp3"),
    IN_GAME_SOUNDTRACK("mp3"),
    GEAR("wav");

    private static final String AUDIO_FOLDER = "resources/audio";
    private final MediaPlayer audio;

    private Sound(String extension) {
        String filePath = AUDIO_FOLDER + "/" + toString() + "." + extension;
        audio = new MediaPlayer(new Media(new File(filePath).toURI().toString()));
    }

    public void play(boolean loop) {
        if (loop)
            audio.setCycleCount(-1);
        else
            audio.setCycleCount(1);

        audio.play();
        audio.seek(Duration.ZERO);
    }

    public void stop() {
        audio.stop();
    }
}