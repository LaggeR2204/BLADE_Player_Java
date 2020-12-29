package sample.Controllers;

import com.sun.glass.ui.CommonDialogs;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
import sample.Model.Playlist;

import java.awt.*;

public class PanelPlaylistViewController {
    @FXML
    private AnchorPane pnlPlaylistView;
    @FXML
    private Label lblPlaylistName;

    private ContextMenu contextMenu;

    @FXML
    private void initialize() {
        contextMenu = new ContextMenu();

        MenuItem addSong = new MenuItem("Add Song");
        MenuItem delete = new MenuItem("Delete");

        addSong.setOnAction((event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All files", "*.*"));
            fileChooser.showOpenMultipleDialog(null);
        });
        contextMenu.getItems().addAll(addSong, delete);

        MenuItem playAll = new MenuItem("Play All");
        contextMenu.getItems().add(playAll);
        pnlPlaylistView.setOnContextMenuRequested(event -> contextMenu.show(pnlPlaylistView, event.getScreenX(), event.getScreenY()));
    }

    public void setPlaylistInfo(Playlist playlist) {
        lblPlaylistName.setText(playlist.getPlaylistName());
    }
}
