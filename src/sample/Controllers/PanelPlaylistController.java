package sample.Controllers;

import javafx.application.Platform;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
        setSelectedPL(currentLibrary.getDefaultPL());
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
        pnlAddPlaylist.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && !tbxPLName.getText().isEmpty()) {
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
                    //JOptionPane.showMessageDialog(null, "This playlist name is already existed.");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Add playlist failed");
                    alert.setHeaderText(null);
                    alert.setContentText("This playlist name is already existed.");

                    alert.showAndWait();
                }
                tbxPLName.clear();
            }
        });
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

    public void resetSelected() {
        for (Node children : this.fpnlListPL.getChildren()) {
            children.setStyle("-fx-background-color: rgb(35,35,35); -fx-border-width: 1px; -fx-border-color: rgb(50,50,50)");
        }
    }

    public void resetFavorite() {
        if (selectedPL != null) {
            setSelectedPL(selectedPL);
        }
        else {
            setSelectedPL(currentLibrary.getDefaultPL());
        }
    }

    public void setSelectedPL(Playlist playlist) {
        selectedPL = playlist;
        loadSong(selectedPL);
    }

    public void removePaneSong(Pane pnlSong) {
        fpnlListSong.getChildren().remove(pnlSong);
    }

    public Playlist getSelectedPL() {
        return selectedPL;
    }

    void loadSong(Playlist playlist) {
        fpnlListSong.getChildren().clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < playlist.getListSong().size(); i++) {
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("../Views/SongViewDetail.fxml"));
                            try {
                                Pane newSong = loader.load();
                                PanelSongViewDetailController controller = loader.getController();
                                controller.setSongInfo(playlist.getListSong().get(i));
                                controller.setParentController(PanelPlaylistController.this);
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
            tbxPLName.requestFocus();
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
                //JOptionPane.showMessageDialog(null, "This playlist name is already existed.");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Add playlist failed");
                alert.setHeaderText(null);
                alert.setContentText("This playlist name is already existed.");

                alert.showAndWait();
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
