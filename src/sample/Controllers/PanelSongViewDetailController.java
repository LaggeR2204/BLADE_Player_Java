package sample.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import sample.Model.AudioQueue;
import sample.Model.Library;
import sample.Model.Playlist;
import sample.Model.Song;
import sample.helper.Helper;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class PanelSongViewDetailController {

    Song _song;
    private PanelPlaylistController _panelPLCtrl;

    @FXML
    private AnchorPane pnlSongDetail;
    @FXML
    private Label lblSongName;
    @FXML
    private Label lblArtist;
    @FXML
    private Label lblGenre;
    @FXML
    private Label lblTime;
    @FXML
    private Button btnPlaySong;
    @FXML
    private Button btnFavoriteSong;
    @FXML
    private Button btnMenuSong;

    private ImageView imageViewWhite;
    private ImageView imageViewOrange;

    @FXML
    private void initialize() {
        btnPlaySong.setVisible(false);
        btnMenuSong.setVisible(false);
        pnlSongDetail.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                btnPlaySong.setVisible(true);
                btnMenuSong.setVisible(true);
                pnlSongDetail.setStyle("-fx-background-color: rgb(60,60,60)");
            } else {
                btnPlaySong.setVisible(false);
                btnMenuSong.setVisible(false);
                pnlSongDetail.setStyle("-fx-background-color: rgb(35,35,35)");
            }
        });

        ContextMenu contextMenu = new ContextMenu();
        MenuItem addToQueueItem = new MenuItem("Add to Queue");
        MenuItem removeItem = new MenuItem("Remove");
        contextMenu.getItems().addAll(addToQueueItem, removeItem);

        btnMenuSong.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                contextMenu.show(pnlSongDetail, event.getScreenX(), event.getScreenY());
            }
        });

        btnPlaySong.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                try {
                    AudioQueue.getInstance().addQueue(_song, true);
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                }
            }
        });

        imageViewWhite = new ImageView(new Image(String.valueOf(getClass().getResource("../img/love_30px.png"))));
        imageViewWhite.setFitWidth(25.0);
        imageViewWhite.setFitHeight(25.0);

        imageViewOrange = new ImageView(new Image(String.valueOf(getClass().getResource("../img/love_orange_30px.png"))));
        imageViewOrange.setFitWidth(25.0);
        imageViewOrange.setFitHeight(25.0);

        removeItem.setOnAction(event -> {
            Playlist playlist = _panelPLCtrl.getSelectedPL();
            if (_song.isFavorite()) {
                Library library = Library.getInstance();
                Playlist favPlaylist = library.getFavoritePL();
                _song.setFavorite(false);
                favPlaylist.getListSong().remove(_song);
            }
            playlist.getListSong().remove(_song);
            _panelPLCtrl.removePaneSong(pnlSongDetail);
        });

        addToQueueItem.setOnAction(event -> {
            try {
                AudioQueue.getInstance().addQueue(_song, false);
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        });
    }

    public void setBtnFavoriteSong(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            if (_panelPLCtrl.getSelectedPL().isFavoritePL()) {
                _panelPLCtrl.removePaneSong(pnlSongDetail);
            }
            setFavoriteSong();
        }
    }

    public void setFavoriteSong() {
        Library library = Library.getInstance();
        Playlist favPlaylist = library.getFavoritePL();
        if (_song.isFavorite()) {
            favPlaylist.getListSong().remove(_song);
            btnFavoriteSong.setGraphic(imageViewWhite);
            _song.setFavorite(false);
        } else {
            favPlaylist.getListSong().add(_song);
            btnFavoriteSong.setGraphic(imageViewOrange);
            _song.setFavorite(true);
        }
    }

    public void setSongInfo(Song song) {
        _song = song;
        if (song.isFavorite()) {
            btnFavoriteSong.setGraphic(imageViewOrange);
        }
        lblSongName.setText(song.getSongName());
        lblArtist.setText(song.getSinger());
        lblGenre.setText(song.getGenre());
        lblTime.setText(Helper.formattedTime(song.getDuration()));
    }

    public void onMouseDoubleClick(MouseEvent mouseEvent)  {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount() == 2){
                try {
                    AudioQueue.getInstance().addQueue(_song, true);
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

    public void setParentController(PanelPlaylistController pnl) {
        _panelPLCtrl = pnl;
    }
}
