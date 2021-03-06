package sample.Controllers;

import com.sun.glass.ui.CommonDialogs;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
import sample.AddSongTask;
import sample.Model.AudioQueue;
import sample.Model.Library;
import sample.Model.Playlist;
import sample.Model.Song;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class PanelPlaylistViewController {
    private PanelPlaylistController _panelPLCtrl;
    @FXML
    private AnchorPane pnlPlaylistView;
    @FXML
    private Label lblPlaylistName;
    @FXML
    private Button btnMenuPlaylist;

    private Playlist _playlist;
    private ContextMenu contextMenu;
    private ContextMenu contextMenuDef;
    private ContextMenu contextMenuFav;

    private List<File> songs;

    @FXML
    private void initialize() {
        contextMenu = new ContextMenu();
        contextMenuDef = new ContextMenu();
        contextMenuFav = new ContextMenu();

        MenuItem addSongItem = new MenuItem("Add Song");
        MenuItem addSongItem2 = new MenuItem("Add song");
        MenuItem deleteItem = new MenuItem("Delete");
        MenuItem playAllItem = new MenuItem("Play All");
        MenuItem playAllItem2 = new MenuItem("Play All");
        MenuItem playAllItem3 = new MenuItem("Play All");

        contextMenu.getItems().addAll(addSongItem, deleteItem, playAllItem);
        contextMenuDef.getItems().addAll(addSongItem2, playAllItem2);
        contextMenuFav.getItems().add(playAllItem3);

        btnMenuPlaylist.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                if (_playlist.isDefaultPL()) {
                    contextMenuDef.show(pnlPlaylistView, event.getScreenX(), event.getScreenY());
                }
                else if (_playlist.isFavoritePL()) {
                    contextMenuFav.show(pnlPlaylistView, event.getScreenX(), event.getScreenY());
                }
                else {
                    contextMenu.show(pnlPlaylistView, event.getScreenX(), event.getScreenY());
                }
                _panelPLCtrl.resetSelected();
                showListSong();
                pnlPlaylistView.setStyle("-fx-background-color: rgb(255,163,26)");
            }
        });

        pnlPlaylistView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                _panelPLCtrl.resetSelected();
                showListSong();
                pnlPlaylistView.setStyle("-fx-background-color: rgb(255,163,26)");
            }
        });

        addSongItem.setOnAction(btnAdd_clicked(addSongItem));
        addSongItem2.setOnAction(btnAdd_clicked(addSongItem2));

        playAllItem.setOnAction(btnPlayAllClick(playAllItem));
        playAllItem2.setOnAction(btnPlayAllClick(playAllItem2));
        playAllItem3.setOnAction(btnPlayAllClick(playAllItem3));

        deleteItem.setOnAction(event -> {
            if (_playlist.isDeletable()) {
                _panelPLCtrl.deletePlaylist(_playlist.getPlaylistName());
            }
        });
    }

    public EventHandler btnPlayAllClick(MenuItem item) {
        EventHandler event = (e) -> {

            try {
                try {
                    AudioQueue.getInstance().addQueue(_playlist.getListSong());
                } catch (UnsupportedAudioFileException unsupportedAudioFileException) {
                    unsupportedAudioFileException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } catch (LineUnavailableException lineUnavailableException) {
                lineUnavailableException.printStackTrace();
            }
        };
        return event;
    }

    public EventHandler btnAdd_clicked(MenuItem item) {
        EventHandler event = (e) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            ExtensionFilter exMp3 = new ExtensionFilter("MP3 files (*.mp3)", "*.mp3");
            ExtensionFilter exWav = new ExtensionFilter("WAV files (*.wav)", "*.wav");
            ExtensionFilter exAll = new ExtensionFilter("All files", "*.*");
            fileChooser.getExtensionFilters().addAll(exMp3, exWav, exAll);

            songs = fileChooser.showOpenMultipleDialog(item.getParentPopup().getScene().getWindow());

            if (songs != null) {
                Task<Void> addSongTask = new AddSongTask(songs, _playlist);
                addSongTask.setOnSucceeded(event1 -> {
                    showListSong();
                });
                Thread thread = new Thread(addSongTask);
                thread.setDaemon(true);
                thread.start();
            }
        };
        return event;
    }

    public Playlist getCurrentPL() {
        return _playlist;
    }

    public void setParentController(PanelPlaylistController pnl) {
        _panelPLCtrl = pnl;
    }

    public void setPlaylistInfo(Playlist playlist) {
        _playlist = playlist;
        lblPlaylistName.setText(playlist.getPlaylistName());
    }

    public void showListSong() {
        if (_panelPLCtrl != null)
            _panelPLCtrl.setSelectedPL(_playlist);
    }
}
