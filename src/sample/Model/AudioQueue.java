package sample.Model;

import sample.audioInterface.INowSongChangeListener;
import sample.audioInterface.IQueueChangeListener;

import java.util.*;

public class AudioQueue {
    //listener
    private ArrayList<IQueueChangeListener> queueListeners;
    private ArrayList<INowSongChangeListener> nowSongListeners;
    private ArrayList<Song> queue;
    private int currentSongPos;
    private boolean isShuffle, isRepeat;
    //Singleton
    private static AudioQueue _instance;

    synchronized public static AudioQueue getInstance() {
        if (_instance == null)
            _instance = new AudioQueue();
        return _instance;
    }

    private AudioQueue() {
        queue = new ArrayList<>();
        queueListeners = new ArrayList<>();
        nowSongListeners = new ArrayList<>();

        isRepeat = false;
        isShuffle = false;
        currentSongPos = -1;
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
    public Song nextSong() {
        if (queue == null || queue.size() == 0)
            return null;
        if (isShuffle && !isRepeat) {
            currentSongPos = getRandom(queue.size() - 1);
        } else if (!isShuffle && !isRepeat) {
            currentSongPos = (currentSongPos + 1) % queue.size();
        }
        if (isValidPosition(currentSongPos)) {
            NotifyNowSongChange();
            return queue.get(currentSongPos);
        }

        return null;
    }

    //choose previous song
    public Song preSong() {
        if (queue == null || queue.size() == 0)
            return null;
        if (isShuffle && !isRepeat) {
            currentSongPos = getRandom(queue.size() - 1);
        } else if (!isShuffle && !isRepeat) {
            currentSongPos = (currentSongPos - 1 + queue.size()) % queue.size();
        }
        if (isValidPosition(currentSongPos)) {
            NotifyNowSongChange();
            return queue.get(currentSongPos);
        }

        return null;
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
    public void addQueue(ArrayList<Song> addList) {
        for (Song song : addList) {
            if (!queue.contains(song)){
                queue.add(song);
                NotifyQueueChange();
            }

        }
    }

    //add a song to queue
    public void addQueue(Song song) {
        if (!queue.contains(song)) {
            queue.add(song);
            NotifyQueueChange();
        }

    }

    //remove a song from queue
    public void removeFromQueue(Song song) {
        if (queue.contains(song)) {
            queue.remove(song);
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
}
