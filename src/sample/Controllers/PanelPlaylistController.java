package sample.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import sample.Model.Library;
import sample.Model.Playlist;

import java.io.IOException;


public class PanelPlaylistController {

    private Library currentLibrary;

    @FXML
    private FlowPane fpnlListPL;

    @FXML
    private void initialize() {
        currentLibrary = Library.getInstance();
        fpnlListPL.setVisible(false);
        try {
            loadPL(currentLibrary);
        } catch (Exception e) {
            System.out.println("ko load dc");
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
}
