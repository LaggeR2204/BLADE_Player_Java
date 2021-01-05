package sample.Controllers;

import javafx.application.Platform;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import sample.Model.Library;
import sample.Model.Playlist;
import sample.Model.Song;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;


public class PanelPlaylistController {

    private Library currentLibrary;
    private Playlist selectedPL;

    @FXML
    private FlowPane fpnlListPL;

    @FXML
    private FlowPane fpnlListSong;

    @FXML
    private Pane pnlAddPlaylist;

    @FXML
    private Pane pnlBGAddPlaylist;

    @FXML
    private TextField tbxPLName;

    @FXML
    private Button btnAddPlaylist;

    @FXML
    private void initialize() {
        currentLibrary = Library.getInstance();
        fpnlListPL.setVisible(false);
        fpnlListSong.getChildren().clear();
        pnlBGAddPlaylist.toBack();
        pnlBGAddPlaylist.setVisible(false);
        BooleanBinding booleanBinding = tbxPLName.textProperty().isEmpty();
        btnAddPlaylist.disableProperty().bind(booleanBinding);
        try {
            loadPL(currentLibrary);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    void loadPL(Library library) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < library.getListPL().size(); i++) {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("../Views/PanelPlaylistView.fxml"));
                            try {
                                Pane newPlaylist = (Pane) loader.load();
                                PanelPlaylistViewController controller = loader.getController();
                                controller.setPlaylistInfo(library.getListPL().get(i));
                                controller.setParentController(PanelPlaylistController.this);
                                fpnlListPL.getChildren().add(newPlaylist);
                            } catch (IOException e) {
                                System.out.print(e.toString());
                            }

                        }
                        fpnlListPL.setVisible(true);
                    }
                });
            }
        }).start();
    }

    public void setSelectedPL(Playlist playlist) {
        selectedPL = playlist;
        LoadSong(selectedPL);
    }

    void LoadSong(Playlist playlist) {
        fpnlListSong.getChildren().clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        fpnlListSong.setVisible(false);
                        for (int i = 0; i < playlist.getListSong().size(); i++) {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("../Views/SongViewDetail.fxml"));
                            try {
                                Pane newSong = loader.load();
                                PanelSongViewDetailController controller = loader.getController();
                                controller.setSongInfo(playlist.getListSong().get(i));
                                fpnlListSong.getChildren().add(newSong);
                            } catch (IOException e) {
                                System.out.print(e.toString());
                            }

                        }
                        fpnlListSong.setVisible(true);
                    }
                });
            }
        }).start();
    }

    public void setBtnAddNewPlaylist(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            pnlBGAddPlaylist.toFront();
            pnlBGAddPlaylist.setVisible(true);
        }
    }

    public void addPlaylist(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            if (!checkExistPlaylist(tbxPLName.getText())) {
                Playlist playlist = new Playlist(tbxPLName.getText(), true);
                currentLibrary.addPlaylistToLibrary(playlist);
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../Views/PanelPlaylistView.fxml"));
                try {
                    Pane newPlaylist = (Pane) loader.load();
                    PanelPlaylistViewController controller = loader.getController();
                    controller.setPlaylistInfo(playlist);
                    controller.setParentController(PanelPlaylistController.this);
                    fpnlListPL.getChildren().add(newPlaylist);
                } catch (IOException e) {
                    System.out.print(e.toString());
                }
                pnlBGAddPlaylist.toBack();
                pnlBGAddPlaylist.setVisible(false);
            }
            else {
                JOptionPane.showMessageDialog(null, "This playlist name is already existed.");
            }
            tbxPLName.clear();
        }
    }

    public boolean checkExistPlaylist(String PLName) {
        for (int i = 0; i < currentLibrary.getListPL().size(); i++) {
            if (currentLibrary.getListPL().get(i).getPlaylistName().equalsIgnoreCase(PLName)) {
                return true;
            }
        }
        return false;
    }

    public void cancelAddNewPlaylist(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            pnlBGAddPlaylist.toBack();
            pnlBGAddPlaylist.setVisible(false);
        }
    }

    public void deletePlaylist(String PLName) {
        for (int i = 0; i < currentLibrary.getListPL().size(); i++) {
            if (currentLibrary.getListPL().get(i).getPlaylistName().equalsIgnoreCase(PLName)) {
                fpnlListPL.getChildren().remove(i);
                currentLibrary.deletePL(i);
            }
        }
    }
}
