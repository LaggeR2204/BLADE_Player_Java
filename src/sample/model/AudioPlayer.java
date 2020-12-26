package sample.model;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayer implements LineListener {
    public static int STATUS_NONE = 0;
    public static int STATUS_PLAY = 1;
    public static int STATUS_PAUSE = 2;

    private static AudioPlayer _instance;

    public static AudioPlayer getInstance() {
        if (_instance == null)
            _instance = new AudioPlayer();
        return _instance;
    }

    // to store current position
    Long currentFrame;
    Clip clip;
    Song song;
    // current status of clip
    int status;

    AudioInputStream audioInputStream;

    private AudioPlayer() {
    }

    public void changeAudio(Song song) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        this.song = song;

        // create AudioInputStream object
        audioInputStream =
                AudioSystem.getAudioInputStream(new File(song.getSongPath()).getAbsoluteFile());

        // create clip reference
        clip = AudioSystem.getClip();
        clip.addLineListener(this);

        // open audioInputStream to the clip
        clip.open(audioInputStream);

        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    // Method to play the audio
    public void play() {
        //start the clip
        clip.start();
        status = STATUS_PLAY;
    }

    // Method to pause the audio
    public void pause() {
        if (status == STATUS_PAUSE) {
            System.out.println("audio is already paused");
            return;
        }
        this.currentFrame =
                this.clip.getMicrosecondPosition();
        clip.stop();
        status = STATUS_PAUSE;
    }

    // Method to resume the audio
    public void resumeAudio() throws UnsupportedAudioFileException,
            IOException, LineUnavailableException {
        if (status == STATUS_PLAY) {
            System.out.println("Audio is already " +
                    "being played");
            return;
        }
        clip.close();
        resetAudioStream();
        clip.setMicrosecondPosition(currentFrame);
        this.play();
    }

    // Method to restart the audio
    public void restart() throws IOException, LineUnavailableException,
            UnsupportedAudioFileException {
        clip.stop();
        clip.close();
        resetAudioStream();
        currentFrame = 0L;
        clip.setMicrosecondPosition(0);
        this.play();
    }

    // Method to stop the audio
    public void stop() throws UnsupportedAudioFileException,
            IOException, LineUnavailableException {
        currentFrame = 0L;
        clip.stop();
        clip.close();
    }

    // Method to jump over a specific part
    public void jump(long c) throws UnsupportedAudioFileException, IOException,
            LineUnavailableException {
        if (c > 0 && c < clip.getMicrosecondLength()) {
            clip.stop();
            clip.close();
            resetAudioStream();
            currentFrame = c;
            clip.setMicrosecondPosition(c);
            this.play();
        }
    }

    // Method to reset audio stream
    public void resetAudioStream() throws UnsupportedAudioFileException, IOException,
            LineUnavailableException {
        audioInputStream = AudioSystem.getAudioInputStream(
                new File(song.getSongPath()).getAbsoluteFile());
        clip.open(audioInputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    @Override
    public void update(LineEvent event) {
    }

    public void addLineEventListenerForClip(LineListener listener) {
        if (clip != null) {
            clip.addLineListener(listener);
        }
    }
}
