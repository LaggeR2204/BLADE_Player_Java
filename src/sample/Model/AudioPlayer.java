package sample.Model;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayer {
    public static int STATUS_NONE = 0;
    public static int STATUS_PLAY = 1;
    public static int STATUS_PAUSE = 2;

    private static AudioPlayer _instance;

    public static AudioPlayer getInstance() throws LineUnavailableException {
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

    private AudioPlayer() throws LineUnavailableException {
        status = STATUS_NONE;
        currentFrame = 0L;
        // create clip reference
        clip = AudioSystem.getClip();
    }

    public int getStatus() {
        return status;
    }

    public Song getSong() {
        return song;
    }

    public void changeAudio(Song song) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        this.song = song;
        if(status != STATUS_NONE) {
            stop();
        }

        resetAudioStream();
        play();
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
        clip.flush();
        status = STATUS_NONE;
    }

    // Method to jump over a specific part
    public void jump(long c) throws UnsupportedAudioFileException, IOException,
            LineUnavailableException {
        if (c >= 0 && c < clip.getMicrosecondLength()) {
            stop();
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
    }

    public void addLineEventListenerForClip(LineListener listener) {
        if (clip != null) {
            clip.addLineListener(listener);
        }
    }

    public int getCurrentSecondDuration() {
        if(clip != null && clip.isRunning())
        {
            return (int) (clip.getMicrosecondPosition() / 1000000);
        }
        return -1;
    }

    public int getTotalSecondDuration() {
        if(clip != null)
        {
            return (int) (clip.getMicrosecondLength() / 1000000);
        }
        return -1;
    }
}
