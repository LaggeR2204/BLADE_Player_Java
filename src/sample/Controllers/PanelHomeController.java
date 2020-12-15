package sample.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.event.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class PanelHomeController {
    private PanelChartController panelChartController;

    @FXML
    private Pane pnlChart;

    @FXML
    private AnchorPane pnlMainHome;

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
        DoubleProperty pnlChartLayoutX = pnlChart.layoutXProperty();
        javafx.animation.KeyValue pnlChartDropDown;
        pnlChartDropDown = new javafx.animation.KeyValue(pnlChartLayoutX, 0);
        new Timeline(new KeyFrame(Duration.seconds(0.5), pnlChartDropDown)).play();
    }

    public void returnHome(){
        DoubleProperty pnlChartLayoutX = pnlChart.layoutXProperty();
        javafx.animation.KeyValue pnlChartDropDown;
        pnlChartDropDown = new javafx.animation.KeyValue(pnlChartLayoutX, 800);
        new Timeline(new KeyFrame(Duration.seconds(0.5), pnlChartDropDown)).play();
    }
}
