package sample.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sample.SearchOnline;
import sample.model.Song;

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
    @FXML
    private ImageView imgSongImage;
    @FXML
    private Pane pnlContent;
    @FXML
    private Label lblSongIndex;

    public void setSongInfo(Song song){
        _song = song;
        lblSongName.setText(song.getSongName());
        lblArtist.setText(song.getSinger());
        lblSongNumber.setText(String.valueOf(song.getSongNumber()));
        if (song.getSongImage() != null){
            imgSongImage.setImage(song.getSongImage());
        }
    }

    public void setSongInfo(Song song, int songIndex){
        pnlContent.setLayoutX(60);
        lblSongIndex.setText(String.valueOf(songIndex));
        if (songIndex == 1){
            lblSongIndex.setTextFill(Color.ORANGE);
        }
        else if (songIndex == 2){
            lblSongIndex.setTextFill(Color.rgb(0,255,0));
        }
        else if (songIndex == 3){
            lblSongIndex.setTextFill(Color.CYAN);
        }
        else if (songIndex == 4){
            lblSongIndex.setTextFill(Color.DARKRED);
        }

        _song = song;
        lblSongName.setText(song.getSongName());
        lblArtist.setText(song.getSinger());
        lblSongNumber.setText(String.valueOf(song.getSongNumber()));
        if (song.getSongImage() != null){
            imgSongImage.setImage(song.getSongImage());
        }
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
