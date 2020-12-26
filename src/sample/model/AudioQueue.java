package sample.model;

import java.util.*;

public class AudioQueue {
    private ArrayList<Song> queue;
    private int currentSongPos;
    private boolean isShuffle, isRepeat;
    //Singleton
    private static AudioQueue _instance;

    public static AudioQueue getInstance() {
        if (_instance == null)
            _instance = new AudioQueue();
        return _instance;
    }

    private AudioQueue() {
        queue = new ArrayList<>();
        isRepeat = false;
        isShuffle = false;
        currentSongPos = -1;
    }

    //check if current position is valid
    public boolean isValidPosition(int pos) {
        if(pos >=0 && pos < queue.size())
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
        if (isValidPosition(currentSongPos))
            return queue.get(currentSongPos);
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
        if (isValidPosition(currentSongPos))
            return queue.get(currentSongPos);
        return null;
    }

    private int getRandom(int i) {
        Random random = new Random();
        return random.nextInt(i + 1);
    }

    //clear all queue
    public void clearQueue(){
        queue.clear();
    }

    //add a list of song to queue
    public void addQueue(ArrayList<Song> addList){
        for (Song song : addList) {
            if(!queue.contains(song))
                queue.add(song);
        }
    }

    //add a song to queue
    public void addQueue(Song song) {
        if(!queue.contains(song))
            queue.add(song);
    }

    //remove a song from queue
    public void removeQueue(Song song) {
        if(queue.contains(song)) {
            queue.remove(song);
        }
    }
}
