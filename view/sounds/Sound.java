package view.sounds;

import java.io.File;

import javafx.animation.Timeline;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sound {
    protected MediaPlayer mp3;
    
    protected Sound(String filePath) {
        mp3 = new MediaPlayer(new Media(new File(filePath).toURI().toString()));
        mp3.setCycleCount(Timeline.INDEFINITE);
    }

    public void play() {
        mp3.play();
    }
}
