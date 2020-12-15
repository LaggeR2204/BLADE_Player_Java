package sample.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sample.SearchOnline;
import sample.Song;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class PanelSongSearchDetailController {
    private SearchOnline searchOnline;
    private Song _song;
    @FXML
    private Label lblSongName;
    @FXML
    private Label lblArtist;
    @FXML
    private Label lblSongNumber;

    public void setSongInfo(Song song){
        _song = song;
        lblSongName.setText(song.getSongName());
        lblArtist.setText(song.getSinger());
        lblSongNumber.setText(String.valueOf(song.getSongNumber()));
    }

    public void btnDownload_Clicked(ActionEvent actionEvent){
        if (_song != null && isConnectedToInternet()){
            Node node = (Node) actionEvent.getSource();
            searchOnline = new SearchOnline();
            searchOnline.downloadSong(_song, (Stage) node.getScene().getWindow());
        }
    }

    private static boolean isConnectedToInternet() {
        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }

}
