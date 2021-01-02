package sample.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.IOException;

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
        pnlHomeBackground.toFront();
        DoubleProperty pnlMainHomeLayoutX = pnlMainHome.layoutXProperty();
        javafx.animation.KeyValue pnlMainHomeDropDown;
        pnlMainHomeDropDown = new javafx.animation.KeyValue(pnlMainHomeLayoutX, -800);
        new Timeline(new KeyFrame(Duration.seconds(0.5), pnlMainHomeDropDown)).play();
    }

    public void returnHome(){
        pnlHomeBackground.toFront();
        DoubleProperty pnlMainHomeLayoutX = pnlMainHome.layoutXProperty();
        javafx.animation.KeyValue pnlMainHomeDropDown;
        pnlMainHomeDropDown = new javafx.animation.KeyValue(pnlMainHomeLayoutX, 0);
        new Timeline(new KeyFrame(Duration.seconds(0.5), pnlMainHomeDropDown)).play();
    }
}
