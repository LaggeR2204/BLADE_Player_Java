package sample.Model;


import javafx.application.Platform;
import sample.audioInterface.IStatusChangeListener;
import sample.helper.Helper;

import javax.sound.sampled.*;
import java.io.*;
import java.util.ArrayList;

public class AudioPlayer {
    public static int STATUS_NONE = 0;
    public static int STATUS_PLAY = 1;
    public static int STATUS_PAUSE = 2;

    private static AudioPlayer _instance;
    private ArrayList<IStatusChangeListener> statusListeners;

    synchronized public static AudioPlayer getInstance() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        if (_instance == null)
            _instance = new AudioPlayer();
        return _instance;
    }

    // to store current position
    private Long currentFrame;
    private Clip clip;
    private Song song;
    // current status of clip
    private int status;
    private float volume;

    private AudioInputStream audioInputStream;

    private AudioPlayer() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        statusListeners = new ArrayList<>();
        status = STATUS_NONE;
        currentFrame = 0L;
        clip = AudioSystem.getClip();
        audioInputStream = null;
        volume = 0.8f;
    }

    public Long getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(Long currentFrame) {
        this.currentFrame = currentFrame;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public float getVolume() {
        return volume;
    }

    public void addStatusChangeListener(IStatusChangeListener listener) {
        if (!statusListeners.contains(listener))
            statusListeners.add(listener);
    }

    public void removeStatusChangeListener(IStatusChangeListener listener) {
        if (statusListeners.contains(listener))
            statusListeners.remove(listener);
    }

    public void NotifyStatusChange() {
        for (IStatusChangeListener ls : statusListeners)
            ls.onStatusChangeListener(this, status);
    }

    public void setStatus(int status) {
        if (this.status != status) {
            this.status = status;
            NotifyStatusChange();
        }
    }

    public int getStatus() {
        return status;
    }

    public Song getSong() {
        return song;
    }

    public void changeAudio(Song song) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        this.song = song;
        if (status != STATUS_NONE) {
            stop();
        }
        resetAudioStream();
        play();
    }

    // Method to play the audio
    public void play() {
        //start the clip
        clip.start();
        setStatus(STATUS_PLAY);
        //status = STATUS_PLAY;
    }

    // Method to pause the audio
    public void pause() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (status == STATUS_PAUSE) {
            System.out.println("audio is already paused");
            return;
        }
        this.currentFrame =
                this.clip.getMicrosecondPosition();
        stop();
        setStatus(STATUS_PAUSE);
        //status = STATUS_PAUSE;
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
        setStatus(STATUS_NONE);
        clip.stop();
        clip.close();
        clip.flush();
        //status = STATUS_NONE;
    }

    // Method to jump over a specific part
    public void jump(long c) throws UnsupportedAudioFileException, IOException,
            LineUnavailableException {
        if (c >= 0 && c < clip.getMicrosecondLength()) {
            int lastStatus = this.status;
            stop();
            resetAudioStream();
            currentFrame = c;
            clip.setMicrosecondPosition(c);
            if(lastStatus == STATUS_PLAY)
                this.play();
            else
                this.pause();
        }
    }

    // Method to reset audio stream
    public void resetAudioStream() throws UnsupportedAudioFileException, IOException,
            LineUnavailableException {
        byte[] data = song.getData();
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        audioInputStream = new AudioInputStream(bais, Helper.getPCMFormat(), data.length / 4);
        clip.open(audioInputStream);
        setVolume(volume);
    }

    public void addLineEventListenerForClip(LineListener listener) {
        if (clip != null) {
            clip.addLineListener(listener);
        }
    }

    public int getCurrentSecondDuration() {
        if (clip != null && clip.isRunning()) {
            return (int) (clip.getMicrosecondPosition() / 1000000);
        }
        return -1;
    }

    public void setVolume(final float volume) {
        this.volume = volume;
        if (clip != null && clip.isOpen()) {
            Platform.runLater(() -> {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float max = gainControl.getMaximum();
                float min = gainControl.getMinimum();
                if(volume == 0)
                {
                    gainControl.setValue(min);
                    return;
                }

                float range = max - min;
                max = max - range * 20 / 100;
                min = min + range * 50 / 100;
                range = max - min;
                float gain = (range * volume) + min;
                gainControl.setValue(gain);
            });
        }
    }
}
