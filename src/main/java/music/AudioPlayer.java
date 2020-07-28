package music;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;

public class AudioPlayer {

    private Clip clip;
    private URL url;

    public AudioPlayer(URL url) {
        this.url = url;
    }

    public boolean isPlaying() {
        return clip != null && clip.isRunning();
    }

    protected void open() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        clip = AudioSystem.getClip();
        clip.open(AudioSystem.getAudioInputStream(url.openStream()));
    }

    public void play() throws IOException, LineUnavailableException, UnsupportedAudioFileException, InterruptedException {
        if (clip == null || !clip.isRunning()) {
            open();
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.flush();
            dispose();
        }
    }

    public void dispose() {
        try {
            clip.close();
        } finally {
            clip = null;
        }
    }

}