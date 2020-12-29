package sample.Model;

import java.util.ArrayList;
import java.util.List;

public class Playlist {

    private String PlaylistName;
    //private List<Song> ListSong;
    private boolean Deletable;

//    public List<Song> getListSong() {
//        return ListSong;
//    }
//
//    public void setListSong(List<Song> listSong) {
//        ListSong = listSong;
//    }

    public String getPlaylistName() {
        return PlaylistName;
    }

    public void setPlaylistName(String playlistName) {
        PlaylistName = playlistName;
    }

    public boolean isDeletable() {
        return Deletable;
    }

    public void setDeletable(boolean deletable) {
        Deletable = deletable;
    }

    public Playlist() {

    }

    public Playlist(String name, boolean deletable) {
        PlaylistName = name;
        //ListSong = null;
        Deletable = deletable;
    }
}
