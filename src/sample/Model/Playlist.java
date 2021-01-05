package sample.Model;

import java.util.*;

public class Playlist {

    private String PlaylistName;
    private List<Song> ListSong;
    private boolean Deletable;

    public List<Song> getListSong() {
        return ListSong;
    }

    public void setListSong(List<Song> listSong) {
        ListSong = listSong;
    }

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

    public void addSongToPL(Song song) {
        this.ListSong.add(song);
    }

    public Playlist() {

    }

    public Playlist(String name, boolean deletable) {
        PlaylistName = name;
        ListSong = new ArrayList<Song>();
        Deletable = deletable;
    }
}
