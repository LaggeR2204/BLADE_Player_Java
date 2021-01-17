package sample.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import sample.Model.AudioQueue;
import sample.Model.Song;
import sample.helper.Helper;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class PanelSongQueueDetailController {

    private Song _song;
    @FXML
    AnchorPane pnlSongQueueDetail;
    @FXML
    private Button btnMenuSong;
    @FXML
    private Label lblSongName;
    @FXML
    private Label lblArtist;
    @FXML
    private Label lblTime;

    @FXML
    private void initialize() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem removeItem = new MenuItem("Remove from Queue");
        contextMenu.getItems().add(removeItem);
        btnMenuSong.setVisible(false);

        pnlSongQueueDetail.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                pnlSongQueueDetail.setStyle("-fx-background-color: rgb(60,60,60)");
                btnMenuSong.setVisible(true);
            } else {
                pnlSongQueueDetail.setStyle("-fx-background-color: rgb(35,35,35)");
                btnMenuSong.setVisible(false);
            }
        });

        btnMenuSong.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                contextMenu.show(pnlSongQueueDetail, event.getScreenX(), event.getScreenY());
            }
        });

        removeItem.setOnAction(event -> {
            try {
                try {
                    AudioQueue.getInstance().removeFromQueue(this._song);
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        });
    }

    public void setSongInfo(Song song) {
        _song = song;
        lblSongName.setText(song.getSongName());
        lblArtist.setText(song.getSinger());
        lblTime.setText(Helper.formattedTime(song.getDuration()));
    }

    public void onMouseDoubleClick(MouseEvent mouseEvent) {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount() == 2){
                try {
                    AudioQueue.getInstance().playSong(_song);
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
