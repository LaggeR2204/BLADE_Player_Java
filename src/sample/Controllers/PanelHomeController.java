package sample.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import javax.swing.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class PanelHomeController {
    private PanelChartController panelChartController;

    @FXML
    private Pane pnlChart;

    @FXML
    private Pane pnlMainHome;

    @FXML
    private Pane pnlHomeBackground;

    @FXML
    public void initialize(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../Views/PanelChart.fxml"));
            pnlChart = (Pane) loader.load();
            panelChartController = loader.getController();
            panelChartController.setParentController(this);
            pnlChart.setLayoutX(800);
            pnlMainHome.getChildren().add(pnlChart);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnChart_Clicked(ActionEvent actionEvent) {
        if (isConnectedToInternet()){
            pnlHomeBackground.toFront();
            DoubleProperty pnlMainHomeLayoutX = pnlMainHome.layoutXProperty();
            javafx.animation.KeyValue pnlMainHomeDropDown;
            pnlMainHomeDropDown = new javafx.animation.KeyValue(pnlMainHomeLayoutX, -800);
            new Timeline(new KeyFrame(Duration.seconds(0.5), pnlMainHomeDropDown)).play();
        }
        else{
            JOptionPane.showMessageDialog(null,"No internet!!!","Error",JOptionPane.ERROR_MESSAGE);
        }

    }

    public void returnHome(){
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
