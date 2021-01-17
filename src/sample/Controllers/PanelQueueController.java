package sample.Controllers;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import sample.Model.AudioQueue;
import sample.Model.Library;
import sample.Model.Playlist;
import sample.Model.Song;
import sample.audioInterface.INowSongChangeListener;
import sample.audioInterface.IQueueChangeListener;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class PanelQueueController implements IQueueChangeListener, INowSongChangeListener {
    private AudioQueue queue;
    private Song _songNow;
    private ImageView imageViewWhite;
    private ImageView imageViewOrange;

    @FXML
    private Label lblSongName;
    @FXML
    private Label lblArtist;
    @FXML
    private FlowPane listQueue;
    @FXML
    private ImageView coverArt;
    @FXML
    private Button btnFavoriteSong;

    @FXML
    private void initialize() {
        try {
            queue = AudioQueue.getInstance();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        queue.addQueueChangeListener(this);
        queue.addNowSongChangeListener(this);

        imageViewWhite = new ImageView(new Image(String.valueOf(getClass().getResource("../img/love_30px.png"))));
        imageViewWhite.setFitWidth(25.0);
        imageViewWhite.setFitHeight(25.0);

        imageViewOrange = new ImageView(new Image(String.valueOf(getClass().getResource("../img/love_orange_30px.png"))));
        imageViewOrange.setFitWidth(25.0);
        imageViewOrange.setFitHeight(25.0);
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
        _songNow = song;
        lblSongName.setText(song.getSongName());
        lblArtist.setText(song.getSinger());
        coverArt.setImage(song.getSongImage());
        coverArt.setVisible(true);
        if (song.isFavorite()) {
            btnFavoriteSong.setGraphic(imageViewOrange);
        }
        else {
            btnFavoriteSong.setGraphic(imageViewWhite);
        }
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
                            loader.setLocation(getClass().getResource("../Views/SongQueueDetail.fxml"));
                            try {
                                Pane newSong = loader.load();
                                PanelSongQueueDetailController controller = loader.getController();
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

    public void setFavoriteSong(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            Library library = Library.getInstance();
            Playlist favPlaylist = library.getFavoritePL();
            if (_songNow.isFavorite()) {
                favPlaylist.getListSong().remove(_songNow);
                btnFavoriteSong.setGraphic(imageViewWhite);
                _songNow.setFavorite(false);
            } else {
                favPlaylist.getListSong().add(_songNow);
                btnFavoriteSong.setGraphic(imageViewOrange);
                _songNow.setFavorite(true);
            }
        }
    }
}
