package sample;

import javafx.concurrent.Task;
import sample.Model.Playlist;
import sample.Model.Song;

import java.io.File;
import java.util.List;

public class AddSongTask extends Task<Void> {
    public List<File> filesSong;
    public Playlist playlist;

    public AddSongTask(List<File> file, Playlist pl) {
        filesSong = file;
        playlist = pl;
    }

    @Override
    protected Void call() {
        if (filesSong != null) {
            for (File f : filesSong) {
                Song temp = new Song(f);
                playlist.addSongToPlaylist(temp);
            }
        }

        return null;
    }
}
