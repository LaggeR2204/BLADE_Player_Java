package sample;

import sample.Model.AudioQueue;
import sample.Model.Song;

import javax.sound.sampled.LineUnavailableException;
import java.util.ArrayList;
import java.util.List;

public class RecentlySongState {
    private List<Song> recentlySongs;

    public void setRecentlySongs(List<Song> recentlySongs) {
        this.recentlySongs = recentlySongs;
    }

    public List<Song> getRecentlySongs() {
        return recentlySongs;
    }

    //Singleton
    private static RecentlySongState _instance;

    synchronized public static RecentlySongState getInstance(){
        if (_instance == null)
            _instance = new RecentlySongState();
        return _instance;
    }

    private RecentlySongState(){
        recentlySongs = new ArrayList<Song>();
    }

    public void addSong(Song song){
        if (song != null){
            if (recentlySongs.isEmpty()){
                recentlySongs.add(song);
            }
            else if (recentlySongs.size() < 3){
                recentlySongs.add(recentlySongs.get(recentlySongs.size()-1));
                recentlySongs.set(0,song);
            }
            else{
                if (!recentlySongs.contains(song)){
                    recentlySongs.set(2, recentlySongs.get(1));
                    recentlySongs.set(1, recentlySongs.get(0));
                    recentlySongs.set(0, song);
                }
                else{
                    int songIndex = recentlySongs.indexOf(song);
                    if (songIndex == 2){
                        recentlySongs.set(2, recentlySongs.get(1));
                        recentlySongs.set(1, recentlySongs.get(0));
                        recentlySongs.set(0, song);
                    }
                    else if (songIndex == 1){
                        recentlySongs.set(1, recentlySongs.get(0));
                        recentlySongs.set(0, song);
                    }
                    else{
                        return;
                    }
                }
            }
        }
    }
}
