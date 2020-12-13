package sample.Controllers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class PanelAboutUsController extends Application {

    @FXML
    private Label lblBLADE;

    public void changeLabelColor(MouseEvent mouseEvent){
        if (mouseEvent.getEventType() == MouseEvent.MOUSE_ENTERED){
            lblBLADE.setFont(new Font("Showcard Gothic", 95));
            lblBLADE.setTextFill(Color.rgb(255,163,26));
        }
        else if (mouseEvent.getEventType() == MouseEvent.MOUSE_EXITED){
            lblBLADE.setFont(new Font("Showcard Gothic", 90));
            lblBLADE.setTextFill(Color.WHITE);
        }

    }

    public void openInfo(ActionEvent actionEvent) {
        Button selectedButton = (Button) actionEvent.getSource();
        switch (selectedButton.getId()){
            case "btnVoThanhBinh":
                getHostServices().showDocument("https://www.facebook.com/22lagger");
                break;
            case "btnBeHaiLong":
                getHostServices().showDocument("https://www.facebook.com/suou.ryuuji");
                break;
            case "btnPhanQuocAn":
                getHostServices().showDocument("https://www.facebook.com/anomg0210");
                break;
            case "btnTruongHuuMinhDuc":
                getHostServices().showDocument("https://www.facebook.com/D9Fury");
                break;
        }
    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}
