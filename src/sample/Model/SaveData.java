package sample.Model;

import javax.sound.sampled.Clip;
import java.util.ArrayList;
import java.util.List;

public class SaveData extends BaseModel {
    //library data
    private List<Playlist> ListPL;
    //queue data
    private ArrayList<Song> queue;
    private int currentSongPos;
    private boolean isShuffle, isRepeat;
    //player data
    private Long currentFrame;
    private Song song;
    private int status;
    private float volume;

    public List<Playlist> getListPL() {
        return ListPL;
    }

    public void setListPL(List<Playlist> listPL) {
        ListPL = listPL;
    }

    public ArrayList<Song> getQueue() {
        return queue;
    }

    public void setQueue(ArrayList<Song> queue) {
        this.queue = queue;
    }

    public int getCurrentSongPos() {
        return currentSongPos;
    }

    public void setCurrentSongPos(int currentSongPos) {
        this.currentSongPos = currentSongPos;
    }

    public boolean isShuffle() {
        return isShuffle;
    }

    public void setShuffle(boolean shuffle) {
        isShuffle = shuffle;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public void setRepeat(boolean repeat) {
        isRepeat = repeat;
    }

    public Long getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(Long currentFrame) {
        this.currentFrame = currentFrame;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }
}
