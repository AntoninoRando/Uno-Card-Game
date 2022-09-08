package view.sounds;

import java.io.File;

import javafx.animation.Timeline;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Sound {
    private MediaPlayer mp3;
    
    public Sound(String filePath, boolean inLoop) {
        mp3 = new MediaPlayer(new Media(new File(filePath).toURI().toString()));
        mp3.setCycleCount(Timeline.INDEFINITE);
    }

    public Sound(String filePath) {
        mp3 = new MediaPlayer(new Media(new File(filePath).toURI().toString()));
    }

    public void play() {
        mp3.play();
        mp3.seek(Duration.ZERO);
    }

    public void stop() {
        mp3.stop();
    }
}
