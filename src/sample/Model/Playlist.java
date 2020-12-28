package sample.model;

import java.util.ArrayList;
import java.util.List;

public class Playlist {

    private String PlaylistName;
    private List<Song> ListSong;

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

    public Playlist() {

    }

    public Playlist(String name, List<Song> songs) {
        PlaylistName = name;
        ListSong = new ArrayList<Song>(songs);
    }
}
