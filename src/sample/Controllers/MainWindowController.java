package sample.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.event.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainWindowController {
    private double xOffset = 0;
    private double yOffset = 0;
    private String selectedButtonId = "btnHome";

    @FXML
    private Button btnClose;

    @FXML
    private Button btnMinimize;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnPlaylist;

    @FXML
    private Button btnCutter;

    @FXML
    private Button btnConverter;

    @FXML
    private Button btnTimer;

    @FXML
    private Button btnAboutUs;

    @FXML
    private Button btnQueue;

    @FXML
    private TextField tbxSearch;

    @FXML
    private Pane pnlSelectedButton;

    @FXML
    private Pane pnlMain;

    @FXML
    private Pane pnlQueueSearchParent;

    @FXML
    private Pane pnlHome;

    @FXML
    private Pane pnlPlaylist;

    @FXML
    private Pane pnlCutter;

    @FXML
    private Pane pnlConverter;

    @FXML
    private Pane pnlAboutUs;

    @FXML
    private Pane pnlQueue;

    @FXML
    private Pane pnlSearch;

    @FXML
    private Pane pnlDropDownTimer;

    @FXML
    private Pane pnlTemp;

    @FXML
    public void initialize()
    {
        tbxSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                if (oldValue.isEmpty()) {
                    switchMainPanel(tbxSearch.getId());
                    return;
                }
                if (newValue.isEmpty()){
                    switchMainPanel(selectedButtonId);
                }
            }
        });
        tbxSearch.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (!tbxSearch.getText().isEmpty())
                {
                    switchMainPanel(tbxSearch.getId());
                }
            }
        });

        try {
            pnlQueue = FXMLLoader.load(getClass().getResource("../Views/PanelQueue.fxml"));
            pnlQueue.setLayoutY(0);
            pnlSearch = FXMLLoader.load(getClass().getResource("../Views/PanelSearch.fxml"));
            pnlSearch.setLayoutY(0);
            pnlQueueSearchParent.getChildren().setAll(pnlQueue, pnlSearch);
            pnlSearch.setVisible(false);
            pnlQueue.setVisible(false);

            pnlHome = FXMLLoader.load(getClass().getResource("../Views/PanelHome.fxml"));
            pnlPlaylist = FXMLLoader.load(getClass().getResource("../Views/PanelPlaylist.fxml"));
            pnlCutter = FXMLLoader.load(getClass().getResource("../Views/PanelCutter.fxml"));
            pnlConverter = FXMLLoader.load(getClass().getResource("../Views/PanelConverter.fxml"));
            pnlAboutUs = FXMLLoader.load(getClass().getResource("../Views/PanelAboutUs.fxml"));
            pnlHome.setLayoutY(0);
            pnlPlaylist.setLayoutY(565);
            pnlCutter.setLayoutY(1130);
            pnlConverter.setLayoutY(1695);
            pnlAboutUs.setLayoutY(2260);
            pnlMain.getChildren().setAll(pnlHome, pnlPlaylist, pnlCutter, pnlConverter, pnlAboutUs);

            switchMainPanel(btnHome.getId());
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void btnClose_Clicked(ActionEvent actionEvent) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    public void btnMinimize_Clicked(ActionEvent actionEvent) {
        Stage stage = (Stage) btnMinimize.getScene().getWindow();
        stage.setIconified(true);
    }

    public void btnHome_Clicked(ActionEvent actionEvent){
        setPnlSelectedButtonPosition(btnHome);
        switchMainPanel(btnHome.getId());
    }

    public void btnPlaylist_Clicked(ActionEvent actionEvent) {
        setPnlSelectedButtonPosition(btnPlaylist);
        switchMainPanel(btnPlaylist.getId());
    }

    public void btnCutter_Clicked(ActionEvent actionEvent) {
        setPnlSelectedButtonPosition(btnCutter);
        switchMainPanel(btnCutter.getId());
    }

    public void btnConverter_Clicked(ActionEvent actionEvent) {
        setPnlSelectedButtonPosition(btnConverter);
        switchMainPanel(btnConverter.getId());
    }


    public void btnTimer_Clicked(ActionEvent actionEvent) {
        setPnlSelectedButtonPosition(btnTimer);
        selectedButtonId = btnTimer.getId();
        dropDownTimerPanel();
    }

    public void btnAboutUs_Clicked(ActionEvent actionEvent) {
        setPnlSelectedButtonPosition(btnAboutUs);
        switchMainPanel(btnAboutUs.getId());
    }

    public void btnQueue_Clicked(ActionEvent actionEvent){
        if (pnlQueue.isVisible()){
            pnlSelectedButton.setVisible(true);
            pnlQueue.setVisible(false);
        }
        else {
            pnlSelectedButton.setVisible(false);
            switchMainPanel(btnQueue.getId());
        }
    }

    public void mousePressed(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    public void dragWindow(MouseEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }

    void setPnlSelectedButtonPosition(Button selectedButton){
        pnlSelectedButton.setVisible(true);
        DoubleProperty pnlSelectedButtonLayoutY = pnlSelectedButton.layoutYProperty();
        javafx.animation.KeyValue pnlSelectedButtonDropDown;
        Double tempLayoutY;
        if (selectedButton == btnAboutUs){
            tempLayoutY = pnlDropDownTimer.getLayoutY();
        }
        else {
            tempLayoutY = selectedButton.getLayoutY();
        }
        pnlSelectedButtonDropDown = new KeyValue(pnlSelectedButtonLayoutY, tempLayoutY);
        new Timeline(new KeyFrame(Duration.seconds(0.25), pnlSelectedButtonDropDown)).play();

    }


    private boolean isDropDown = false;
    void dropDownTimerPanel()
    {
        DoubleProperty btnAboutUsLayoutY = btnAboutUs.layoutYProperty();
        javafx.animation.KeyValue btnAboutUsDropDown;
        DoubleProperty pnlTempLayoutY = pnlTemp.layoutYProperty();
        javafx.animation.KeyValue pnlTempDropDown;
        if (isDropDown == true){
            btnAboutUsDropDown = new javafx.animation.KeyValue(btnAboutUsLayoutY, 340);
            pnlTempDropDown = new javafx.animation.KeyValue(pnlTempLayoutY, 340);
        }
        else {
            btnAboutUsDropDown = new javafx.animation.KeyValue(btnAboutUsLayoutY, 480);
            pnlTempDropDown = new javafx.animation.KeyValue(pnlTempLayoutY, 480);
        }
        new Timeline(new KeyFrame(Duration.seconds(0.25), btnAboutUsDropDown)).play();
        new Timeline(new KeyFrame(Duration.seconds(0.25), pnlTempDropDown)).play();
        isDropDown = !isDropDown;
    }

    void switchMainPanel(String btnId){
        if (isDropDown){
            dropDownTimerPanel();
        }
        DoubleProperty pnlMainLayoutY = pnlMain.layoutYProperty();
        javafx.animation.KeyValue pnlMainDropDown;
        switch (btnId) {
            case "btnHome":
                pnlMainDropDown = new KeyValue(pnlMainLayoutY, 0);
                new Timeline(new KeyFrame(Duration.seconds(0.5), pnlMainDropDown)).play();

                pnlQueue.setVisible(false);
                pnlSearch.setVisible(false);

                selectedButtonId = btnId;
                pnlMain.toFront();
                break;
            case "btnPlaylist":
                pnlMainDropDown = new KeyValue(pnlMainLayoutY, - 565);
                new Timeline(new KeyFrame(Duration.seconds(0.5), pnlMainDropDown)).play();

                pnlQueue.setVisible(false);
                pnlSearch.setVisible(false);

                selectedButtonId = btnId;
                pnlMain.toFront();
                break;
            case "btnCutter":
                pnlMainDropDown = new KeyValue(pnlMainLayoutY, - 565*2);
                new Timeline(new KeyFrame(Duration.seconds(0.5), pnlMainDropDown)).play();

                pnlQueue.setVisible(false);
                pnlSearch.setVisible(false);

                selectedButtonId = btnId;
                pnlMain.toFront();
                break;
            case "btnConverter":
                pnlMainDropDown = new KeyValue(pnlMainLayoutY, - 565*3);
                new Timeline(new KeyFrame(Duration.seconds(0.5), pnlMainDropDown)).play();

                pnlQueue.setVisible(false);
                pnlSearch.setVisible(false);

                selectedButtonId = btnId;
                pnlMain.toFront();
                break;
            case "btnAboutUs":
                pnlMainDropDown = new KeyValue(pnlMainLayoutY, - 565*4);
                new Timeline(new KeyFrame(Duration.seconds(0.5), pnlMainDropDown)).play();

                pnlQueue.setVisible(false);
                pnlSearch.setVisible(false);

                selectedButtonId = btnId;
                pnlMain.toFront();
                break;
            case "btnQueue":
                pnlQueueSearchParent.toFront();
                pnlQueue.setVisible(true);
                pnlQueue.toFront();
                break;
            case "tbxSearch":
                pnlQueueSearchParent.toFront();
                pnlSearch.setVisible(true);
                pnlSearch.toFront();
                break;
        }
    }
}
