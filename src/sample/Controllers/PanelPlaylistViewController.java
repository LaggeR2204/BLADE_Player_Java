package sample.Controllers;

import com.sun.glass.ui.CommonDialogs;
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
import sample.Model.Playlist;
import sample.Model.Song;

import java.io.File;
import java.util.List;

public class PanelPlaylistViewController {
    private PanelPlaylistController _panelPLCtrl;
    @FXML
    private AnchorPane pnlPlaylistView;
    @FXML
    private Label lblPlaylistName;

    private Playlist _playlist;
    private ContextMenu contextMenu;
    private List<File> songs;

    @FXML
    private void initialize() {
        contextMenu = new ContextMenu();

        MenuItem addSong = new MenuItem("Add Song");
        MenuItem delete = new MenuItem("Delete");
        contextMenu.getItems().addAll(addSong, delete);

        MenuItem playAll = new MenuItem("Play All");
        contextMenu.getItems().add(playAll);
        pnlPlaylistView.setOnContextMenuRequested(event -> contextMenu.show(pnlPlaylistView, event.getScreenX(), event.getScreenY()));

        pnlPlaylistView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY)
            {
                showListSong();
            }
        });

        addSong.setOnAction((event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            ExtensionFilter exMp3 = new ExtensionFilter("MP3 files (*.mp3)", "*.mp3");
            ExtensionFilter exWav = new ExtensionFilter("WAV files (*.wav)", "*.wav");
            ExtensionFilter exAll = new ExtensionFilter("All files", "*.*");
            fileChooser.getExtensionFilters().addAll(exMp3, exWav, exAll);
            songs = fileChooser.showOpenMultipleDialog(((MenuItem)event.getTarget()).getParentPopup().getScene().getWindow());
            if (songs != null) {
                for (File f : songs) {
                    Song temp = new Song(f);
                    _playlist.addSongToPL(temp);
                }
            }
        });
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
