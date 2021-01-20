package sample;

import javafx.concurrent.Task;
import sample.Model.Playlist;
import sample.Model.Song;
import sample.helper.Helper;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
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
                Song temp = null;
                try {
                    temp = new Song(f);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                }
                if(temp != null)
                    playlist.addSongToPlaylist(temp);
            }
        }

        return null;
    }
}
