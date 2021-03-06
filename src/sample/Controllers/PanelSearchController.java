package sample.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import sample.SearchOnline;
import sample.Model.Song;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PanelSearchController {
    private SearchOnline searchOnline = new SearchOnline();

    private List<Song> listSongSearch = new ArrayList<Song>();

    @FXML
    private Label lblTextSearch;

    @FXML
    private Pane pnlSearchResult;

    @FXML
    private FlowPane fpnlSearchItems;

    @FXML
    private Pane pnlLoading;

    public void cancelSearch(){
        pnlSearchResult.setVisible(false);
        lblTextSearch.setText("");
        fpnlSearchItems.getChildren().clear();
        listSongSearch.clear();
        pnlLoading.setVisible(false);
    }


    public void search(String textSearch) throws IOException{
        lblTextSearch.setText(textSearch);
        fpnlSearchItems.getChildren().clear();
        listSongSearch.clear();
        pnlSearchResult.setVisible(false);
        pnlLoading.setVisible(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                listSongSearch = searchOnline.search(textSearch);
                if (!listSongSearch.isEmpty()){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (!listSongSearch.isEmpty()){
                                for (Song item: listSongSearch) {
                                    FXMLLoader loader = new FXMLLoader();
                                    loader.setLocation(getClass().getResource("../Views/PanelSongSearchDetail.fxml"));

                                    try{
                                        Pane newSongSearchDetail = (Pane) loader.load();
                                        PanelSongSearchDetailController controller = loader.getController();
                                        controller.setSongInfo(item);
                                        fpnlSearchItems.getChildren().add(newSongSearchDetail);
                                    }
                                    catch (IOException e){
                                        System.out.print(e.toString());
                                    }
                                    pnlSearchResult.setVisible(true);
                                    pnlLoading.setVisible(false);
                                }
                            }
                        }
                    });
                }
                else {
                    pnlLoading.setVisible(false);
                    JOptionPane.showMessageDialog(null, "No results for the keyword: " + textSearch);
                }
            }
        }).start();
    }
}
