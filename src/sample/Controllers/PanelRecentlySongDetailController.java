package sample.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import sample.Model.AudioQueue;
import sample.Model.Song;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class PanelRecentlySongDetailController {
    @FXML
    private ImageView imgSongImage;
    @FXML
    private Label lblSongName;

    private Song song;

    @FXML
    private void initialize(){
        clearInfo();
        song = null;
    }

    public Song getSong(){
        return song;
    }

    public void setSong(Song _song){
        song = _song;
        if (song != null){
            imgSongImage.setImage(song.getSongImage());
            lblSongName.setText(song.getSongName());
        }
    }

    public void clearInfo(){
        song = null;
        imgSongImage.setImage(null);
        lblSongName.setText("");
    }

    //play song and add to queue
    public void onMouseDoubleClick(MouseEvent mouseEvent)  {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY) && song != null){
            if(mouseEvent.getClickCount() == 2){
                try {
                    AudioQueue.getInstance().addQueue(song, true);
                    AudioQueue.getInstance().playSong(song);
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
