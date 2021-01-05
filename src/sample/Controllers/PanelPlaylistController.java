package sample.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import sample.Model.Library;
import sample.Model.Playlist;
import sample.Model.Song;

import java.io.IOException;


public class PanelPlaylistController {

    private Library currentLibrary;
    private Playlist selectedPL;

    @FXML
    private FlowPane fpnlListPL;

    @FXML
    private FlowPane fpnlListSong;

    @FXML
    private void initialize() {
        currentLibrary = Library.getInstance();
        fpnlListPL.setVisible(false);
        fpnlListSong.getChildren().clear();

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
}
