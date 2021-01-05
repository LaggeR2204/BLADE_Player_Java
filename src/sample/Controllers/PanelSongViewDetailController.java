package sample.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sample.Model.Song;
import sample.helper.Helper;

public class PanelSongViewDetailController {

    Song _song;
    @FXML
    private Label lblSongName;
    @FXML
    private Label lblArtist;
    @FXML
    private Label lblGenre;
    @FXML
    private Label lblTime;
    public void setSongInfo(Song song) {
        _song = song;
        lblSongName.setText(song.getSongName());
        lblArtist.setText(song.getSinger());
        lblGenre.setText(song.getGenre());
        lblTime.setText(Helper.formattedTime(song.getDuration()));
    }
}
