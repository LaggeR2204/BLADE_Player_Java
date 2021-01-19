package sample.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import sample.Model.Song;
import sample.RecentlySongState;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class PanelHomeController {
    private PanelChartController panelChartController;
    private PanelRecentlySongDetailController pnlRecentlySong1Controller;
    private PanelRecentlySongDetailController pnlRecentlySong2Controller;
    private PanelRecentlySongDetailController pnlRecentlySong3Controller;
    @FXML
    private Pane pnlChart;

    @FXML
    private Pane pnlMainHome;

    @FXML
    private Pane pnlRecentlySong1;
    @FXML
    private Pane pnlRecentlySong2;
    @FXML
    private Pane pnlRecentlySong3;

    @FXML
    private Pane pnlHomeBackground;

    @FXML
    public void initialize() {
        try {
            FXMLLoader loader1 = new FXMLLoader();
            loader1.setLocation(getClass().getResource("../Views/RecentlySongDetail.fxml"));
            pnlRecentlySong1 = (Pane) loader1.load();
            pnlRecentlySong1Controller = loader1.getController();
            pnlRecentlySong1.setLayoutX(50);
            pnlRecentlySong1.setLayoutY(40);
            FXMLLoader loader2 = new FXMLLoader();
            loader2.setLocation(getClass().getResource("../Views/RecentlySongDetail.fxml"));
            pnlRecentlySong2 = (Pane) loader2.load();
            pnlRecentlySong2Controller = loader2.getController();
            pnlRecentlySong2.setLayoutX(306);
            pnlRecentlySong2.setLayoutY(40);
            FXMLLoader loader3 = new FXMLLoader();
            loader3.setLocation(getClass().getResource("../Views/RecentlySongDetail.fxml"));
            pnlRecentlySong3 = (Pane) loader3.load();
            pnlRecentlySong3Controller = loader3.getController();
            pnlRecentlySong3.setLayoutX(556);
            pnlRecentlySong3.setLayoutY(40);
            pnlHomeBackground.getChildren().setAll(pnlRecentlySong1, pnlRecentlySong2, pnlRecentlySong3);
            FXMLLoader loaderChart = new FXMLLoader();
            loaderChart.setLocation(getClass().getResource("../Views/PanelChart.fxml"));
            pnlChart = (Pane) loaderChart.load();
            panelChartController = loaderChart.getController();
            panelChartController.setParentController(this);
            pnlChart.setLayoutX(800);
            pnlMainHome.getChildren().add(pnlChart);

            setRecentlySongs(RecentlySongState.getInstance().getRecentlySongs());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRecentlySong(Song song) {
        if (song != null) {
            if (song == pnlRecentlySong1Controller.getSong()) {
            } else if (song == pnlRecentlySong2Controller.getSong()) {
                pnlRecentlySong2Controller.setSong(pnlRecentlySong1Controller.getSong());
                pnlRecentlySong1Controller.setSong(song);
            } else {
                pnlRecentlySong3Controller.setSong(pnlRecentlySong2Controller.getSong());
                pnlRecentlySong2Controller.setSong(pnlRecentlySong1Controller.getSong());
                pnlRecentlySong1Controller.setSong(song);
            }
        }
        RecentlySongState.getInstance().addSong(song);
        return;
    }

    public void setRecentlySongs(List<Song> songs) {
        if (songs == null)
            return;
        Song song1 = null;
        Song song2 = null;
        Song song3 = null;
        if (songs.size() >= 1)
            song1 = songs.get(0);
        if (songs.size() >= 2)
            song2 = songs.get(1);
        if (songs.size() >= 3)
            song3 = songs.get(2);
        if (song1 != null) {
            pnlRecentlySong1Controller.setSong(song1);
            if (song2 != null) {
                pnlRecentlySong2Controller.setSong(song2);
                if (song3 != null) {
                    pnlRecentlySong3Controller.setSong(song3);
                    return;
                } else {
                    return;
                }
            } else if (song3 != null) {
                pnlRecentlySong2Controller.setSong(song3);
                return;
            } else {
                return;
            }
        } else {
            if (song2 != null) {
                pnlRecentlySong1Controller.setSong(song2);
                if (song3 != null) {
                    pnlRecentlySong2Controller.setSong(song3);
                    return;
                } else {
                    return;
                }
            } else if (song3 != null) {
                pnlRecentlySong1Controller.setSong(song3);
                return;
            } else {
                return;
            }
        }
    }

    public void btnChart_Clicked(ActionEvent actionEvent) {
        if (isConnectedToInternet()) {
            pnlHomeBackground.toFront();
            DoubleProperty pnlMainHomeLayoutX = pnlMainHome.layoutXProperty();
            javafx.animation.KeyValue pnlMainHomeDropDown;
            pnlMainHomeDropDown = new javafx.animation.KeyValue(pnlMainHomeLayoutX, -800);
            new Timeline(new KeyFrame(Duration.seconds(0.5), pnlMainHomeDropDown)).play();
        } 
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Search failed");
            alert.setHeaderText(null);
            alert.setContentText("No internet !!!");

            alert.showAndWait();
        }

    }

    public void returnHome() {
        pnlHomeBackground.toFront();
        DoubleProperty pnlMainHomeLayoutX = pnlMainHome.layoutXProperty();
        javafx.animation.KeyValue pnlMainHomeDropDown;
        pnlMainHomeDropDown = new javafx.animation.KeyValue(pnlMainHomeLayoutX, 0);
        new Timeline(new KeyFrame(Duration.seconds(0.5), pnlMainHomeDropDown)).play();
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
