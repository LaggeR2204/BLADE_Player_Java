package sample.Model;

import sample.audioInterface.INowSongChangeListener;
import sample.audioInterface.IQueueChangeListener;

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

public class AudioQueue implements LineListener {
    //listener
    transient private ArrayList<IQueueChangeListener> queueListeners;
    transient private ArrayList<INowSongChangeListener> nowSongListeners;
    private ArrayList<Song> queue;
    private int currentSongPos;
    private boolean isShuffle, isRepeat;
    //Singleton
    private static AudioQueue _instance;

    synchronized public static AudioQueue getInstance() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        if (_instance == null)
            _instance = new AudioQueue();
        return _instance;
    }

    private AudioQueue() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        queueListeners = new ArrayList<>();
        nowSongListeners = new ArrayList<>();
        queue = new ArrayList<>();
        isRepeat = false;
        isShuffle = false;
        currentSongPos = -1;
        AudioPlayer.getInstance().addLineEventListenerForClip(this);
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public void setQueue(ArrayList<Song> queue) {
        this.queue = queue;
    }

    public void setCurrentSongPos(int currentSongPos) {
        this.currentSongPos = currentSongPos;
    }

    public void setRepeat(boolean repeat) {
        isRepeat = repeat;
    }

    public int getCurrentSongPos() {
        return currentSongPos;
    }

    public ArrayList<Song> getQueue() {
        return queue;
    }

    public void setShuffle(boolean shuffle) {
        isShuffle = shuffle;
    }

    public boolean isShuffle() {
        return isShuffle;
    }

    //check if current position is valid
    public boolean isValidPosition(int pos) {
        if (pos >= 0 && pos < queue.size())
            return true;
        return false;
    }

    public Song getCurrentSong() {
        if (isValidPosition(currentSongPos)) {
            return queue.get(currentSongPos);
        }
        return null;
    }

    //choose next song
    public Song nextSong() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (queue == null || queue.size() == 0)
            return null;
        if (isShuffle && !isRepeat) {
            int temp = getRandom(queue.size() - 1);
            while (temp == currentSongPos) {
                temp = getRandom(queue.size() - 1);
            }
            currentSongPos = temp;
        } else if (!isShuffle && !isRepeat) {
            currentSongPos = (currentSongPos + 1) % queue.size();
        }
        if (isValidPosition(currentSongPos)) {
            AudioPlayer.getInstance().changeAudio(queue.get(currentSongPos));
            NotifyNowSongChange();
            return queue.get(currentSongPos);
        }
        return null;
    }

    //choose previous song
    public Song preSong() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (queue == null || queue.size() == 0)
            return null;
        if (isShuffle && !isRepeat) {
            int temp = getRandom(queue.size() - 1);
            while (temp == currentSongPos) {
                temp = getRandom(queue.size() - 1);
            }
            currentSongPos = temp;
        } else if (!isShuffle && !isRepeat) {
            currentSongPos = (currentSongPos - 1 + queue.size()) % queue.size();
        }
        if (isValidPosition(currentSongPos)) {
            NotifyNowSongChange();
            AudioPlayer.getInstance().changeAudio(queue.get(currentSongPos));
            return queue.get(currentSongPos);
        }

        return null;
    }

    public int playSong(Song song) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (queue.contains(song)) {
            currentSongPos = queue.indexOf(song);
            AudioPlayer.getInstance().changeAudio(song);
            NotifyNowSongChange();
            return 1;
        }
        return -1;
    }

    private int getRandom(int i) {
        Random random = new Random();
        return random.nextInt(i + 1);
    }

    //clear all queue
    public void clearQueue() {
        if (queue.size() > 0) {
            queue.clear();
            NotifyQueueChange();
        }
    }

    //add a list of song to queue
    public void addQueue(Collection<Song> addList) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        int num = addList.size();
        for (Song song : addList) {
            if (!queue.contains(song)) {
                queue.add(song);
                num--;
            }
        }
        if (num != addList.size())
            NotifyQueueChange();
        if (queue.size() == addList.size())
            nextSong();
    }

    //add a song to queue
    public void addQueue(Song song, boolean play) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (!queue.contains(song)) {
            queue.add(song);
            NotifyQueueChange();
        }
        if (play) {
            currentSongPos = queue.indexOf(song);
            AudioPlayer.getInstance().changeAudio(song);
            NotifyNowSongChange();
        }
    }

    //remove a song from queue
    public void removeFromQueue(Song song) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (queue.contains(song)) {
            if (song == queue.get(currentSongPos)) {
                if (queue.size() > 1)
                    nextSong();
                else {
                    AudioPlayer.getInstance().stop();
                    currentSongPos = -1;
                    NotifyNowSongChange();
                }
            }
            queue.remove(song);
            if(AudioPlayer.getInstance().getSong() != null)
                currentSongPos = queue.indexOf(AudioPlayer.getInstance().getSong());
            NotifyQueueChange();
        }
    }

    //add and remove listener
    public void addQueueChangeListener(IQueueChangeListener listener) {
        if (!queueListeners.contains(listener))
            queueListeners.add(listener);
    }

    public void removeQueueChangeListener(IQueueChangeListener listener) {
        if (queueListeners.contains(listener))
            queueListeners.remove(listener);
    }

    public void addNowSongChangeListener(INowSongChangeListener listener) {
        if (!nowSongListeners.contains(listener))
            nowSongListeners.add(listener);
    }

    public void removeNowSongChangeListener(INowSongChangeListener listener) {
        if (nowSongListeners.contains(listener))
            nowSongListeners.remove(listener);
    }

    //Notify to listener
    public void NotifyQueueChange() {
        for (IQueueChangeListener ls : queueListeners)
            ls.onQueueChangeListener(this, queue);
    }

    public void NotifyNowSongChange() {
        for (INowSongChangeListener ls : nowSongListeners)
            ls.onNowSongChangeListener(this, getCurrentSong());
    }

    @Override
    public void update(LineEvent event) {
        try {
            if (event.getType() == LineEvent.Type.STOP && AudioPlayer.getInstance().getStatus() != AudioPlayer.STATUS_NONE) {
                System.out.println("Stop event clip");
                nextSong();
            }
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
