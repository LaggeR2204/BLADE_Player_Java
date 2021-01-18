package sample.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import sample.ChartOnline;
import sample.Model.Song;

import javax.swing.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class PanelChartController {
    private PanelHomeController panelHomeController;
    private Button selectedButton;
    private ChartOnline chartOnline;
    private List<Song> listSongVN;
    private List<Song> listSongUSUK;
    private List<Song> listSongKorea;

    @FXML
    private Button btnVN;
    @FXML
    private Button btnUSUK;

    @FXML
    private Pane pnlSelectedButton;

    @FXML
    private Pane pnlDropDownChart;

    @FXML
    private FlowPane fpnlVNItems;

    @FXML
    private FlowPane fpnlUSUKItems;

    @FXML
    private FlowPane fpnlKoreaItems;

    @FXML
    private ScrollPane spnlVN;

    @FXML
    private ScrollPane spnlUSUK;

    @FXML
    private ScrollPane spnlKorea;

    @FXML
    private void initialize(){
        selectedButton = btnVN;
        chartOnline = new ChartOnline();
        listSongVN = new ArrayList<Song>();
        listSongUSUK = new ArrayList<Song>();
        listSongKorea = new ArrayList<Song>();

        if (isConnectedToInternet()){
            refreshAllChart();
        }

    }

    public void setParentController(PanelHomeController _panelHomeController){
        this.panelHomeController = _panelHomeController;
    }

    public void btnRefreshAll_Clicked(ActionEvent actionEvent){
        refreshAllChart();
    }

    public void btnReturn_Clicked(ActionEvent actionEvent) {
        if (panelHomeController != null){
            panelHomeController.returnHome();
        }
    }

    public void changeAreaChart(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        if (selectedButton == btn){
            if (btn == btnVN){
                refreshVNChart();
            }
            else if (btn == btnUSUK){
                refreshUSUKChart();
            }
            else {
                refreshKoreaChart();
            }
        }
        dropdownPanelSelectedButton(btn.getLayoutX());
        if (btn == btnVN){
            dropdownPanelContent(0);
        }
        else if (btn == btnUSUK){
            dropdownPanelContent(-800);
        }
        else {
            dropdownPanelContent(-1600);
        }
        selectedButton = btn;
    }

    private void dropdownPanelSelectedButton(double btnLayoutX){
        DoubleProperty pnlSelectedButtonLayoutX = pnlSelectedButton.layoutXProperty();
        javafx.animation.KeyValue pnlSelectedButtonDropDown = new javafx.animation.KeyValue(pnlSelectedButtonLayoutX, btnLayoutX);
        new Timeline(new KeyFrame(Duration.seconds(0.5), pnlSelectedButtonDropDown)).play();
    }

    private void dropdownPanelContent(double pnlLayoutX){
        DoubleProperty pnlDropDownChartLayoutX = pnlDropDownChart.layoutXProperty();
        javafx.animation.KeyValue pnlDropDownChartDropDown = new javafx.animation.KeyValue(pnlDropDownChartLayoutX, pnlLayoutX);
        new Timeline(new KeyFrame(Duration.seconds(0.5), pnlDropDownChartDropDown)).play();
    }

    private void refreshVNChart(){
        if (isConnectedToInternet()){
            spnlVN.setVisible(false);
            fpnlVNItems.getChildren().clear();
            listSongVN.clear();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    listSongVN = chartOnline.crawlVietNamChart();

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (!listSongVN.isEmpty()){
                                int songIndex = 1;
                                for (Song item:listSongVN) {

                                    FXMLLoader loader = new FXMLLoader();
                                    loader.setLocation(getClass().getResource("../Views/PanelSongSearchDetail.fxml"));

                                    try {
                                        Pane newSongSearchDetail = (Pane) loader.load();
                                        PanelSongSearchDetailController controller = loader.getController();
                                        controller.setSongInfo(item, songIndex);
                                        songIndex++;
                                        fpnlVNItems.getChildren().add(newSongSearchDetail);
                                    } catch (IOException e) {
                                        System.out.print(e.toString());
                                    }
                                }
                            }
                            spnlVN.setVisible(true);
                        }
                    });
                }
            }).start();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Search failed");
            alert.setHeaderText(null);
            alert.setContentText("No internet !!!");

            alert.showAndWait();
        }


    }

    private void refreshUSUKChart(){
        if (isConnectedToInternet()){
            spnlUSUK.setVisible(false);
            fpnlUSUKItems.getChildren().clear();
            listSongUSUK.clear();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    listSongUSUK = chartOnline.crawlUSUKChart();

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (!listSongUSUK.isEmpty()){
                                int songIndex = 1;
                                for (Song item:listSongUSUK) {
                                    FXMLLoader loader = new FXMLLoader();
                                    loader.setLocation(getClass().getResource("../Views/PanelSongSearchDetail.fxml"));

                                    try {
                                        Pane newSongSearchDetail = (Pane) loader.load();
                                        PanelSongSearchDetailController controller = loader.getController();
                                        controller.setSongInfo(item, songIndex);
                                        songIndex++;
                                        fpnlUSUKItems.getChildren().add(newSongSearchDetail);
                                    } catch (IOException e) {
                                        System.out.print(e.toString());
                                    }
                                }
                            }
                            spnlUSUK.setVisible(true);
                        }
                    });
                }
            }).start();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Search failed");
            alert.setHeaderText(null);
            alert.setContentText("No internet !!!");

            alert.showAndWait();
        }


    }

    private void refreshKoreaChart(){

        if (isConnectedToInternet()){
            spnlKorea.setVisible(false);
            fpnlKoreaItems.getChildren().clear();
            listSongKorea.clear();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    listSongKorea = chartOnline.crawlKoreaChart();

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (!listSongKorea.isEmpty()){
                                int songIndex = 1;
                                for (Song item:listSongKorea) {
                                    FXMLLoader loader = new FXMLLoader();
                                    loader.setLocation(getClass().getResource("../Views/PanelSongSearchDetail.fxml"));

                                    try {
                                        Pane newSongSearchDetail = (Pane) loader.load();
                                        PanelSongSearchDetailController controller = loader.getController();
                                        controller.setSongInfo(item, songIndex);
                                        songIndex++;
                                        fpnlKoreaItems.getChildren().add(newSongSearchDetail);
                                    } catch (IOException e) {
                                        System.out.print(e.toString());
                                    }
                                }
                            }
                            spnlKorea.setVisible(true);
                        }
                    });
                }
            }).start();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Search failed");
            alert.setHeaderText(null);
            alert.setContentText("No internet !!!");

            alert.showAndWait();
        }


    }

    private void refreshAllChart(){

        if (isConnectedToInternet()){
            refreshVNChart();
            refreshUSUKChart();
            refreshKoreaChart();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Search failed");
            alert.setHeaderText(null);
            alert.setContentText("No internet !!!");

            alert.showAndWait();
        }

    }

    private static boolean isConnectedToInternet() {
        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }
}
