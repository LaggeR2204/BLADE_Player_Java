package sample.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import sample.Model.AudioQueue;
import sample.Model.Song;
import sample.audioInterface.INowSongChangeListener;
import sample.audioInterface.IQueueChangeListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class PanelQueueController implements IQueueChangeListener, INowSongChangeListener {
    private AudioQueue queue;
    @FXML
    private Label lblSongName;
    @FXML
    private Label lblArtist;
    @FXML
    private FlowPane listQueue;
    @FXML
    private ImageView coverArt;

    @FXML
    private void initialize() {
        queue = AudioQueue.getInstance();
        queue.addQueueChangeListener(this);
        queue.addNowSongChangeListener(this);
    }

    @Override
    public void onQueueChangeListener(Object sender, Collection<Song> newQueue) {
        RefreshQueue(newQueue);
    }

    @Override
    public void onNowSongChangeListener(Object sender, Song newSong) {
        ShowNowSongInfo(newSong);
    }

    public void ShowNowSongInfo(Song song)
    {
        if(song == null) {
            lblSongName.setText("...");
            lblArtist.setText("...");
            coverArt.setVisible(false);
            return;
        }
        lblSongName.setText(song.getSongName());
        lblArtist.setText(song.getSinger());
        coverArt.setImage(song.getSongImage());
        coverArt.setVisible(true);
    }

    public void RefreshQueue(Collection<Song> newQueue)
    {
        listQueue.getChildren().clear();
        ArrayList<Song> songs = (ArrayList<Song>)newQueue;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < songs.size(); i++) {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("../Views/SongViewDetail.fxml"));
                            try {
                                Pane newSong = loader.load();
                                PanelSongViewDetailController controller = loader.getController();
                                controller.setSongInfo(songs.get(i));
                               // controller.setParentController(PanelQueueController.this);
                                listQueue.getChildren().add(newSong);
                            } catch (IOException e) {
                                System.out.print(e.toString());
                            }
                        }
                        listQueue.setVisible(true);
                    }
                });
            }
        }).start();
    }
}
