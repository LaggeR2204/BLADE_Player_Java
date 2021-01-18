package sample.Controllers;

import com.jfoenix.controls.JFXSlider;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Timer;
import javax.swing.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.JOptionPane;
import sample.ExitAppTask;

public class MainWindowController {
    private double xOffset = 0;
    private double yOffset = 0;
    private Timer sTimer;
    private ExitAppTask exitTask = new ExitAppTask();
    private double sec;
    private String selectedButtonId = "btnHome";
    private PanelSearchController searchController;
    private NowPlayingController nowPlayingController;
    private PanelQueueController panelQueueController;
    @FXML
    private Pane pnlLogo;

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
    private Button btnStopTimer;

    @FXML
    private Button btnAboutUs;

    @FXML
    private Button btnQueue;

    @FXML
    private TextField tbxSearch;

    @FXML
    private Pane pnlSelectedButton;

    @FXML
    private Pane pnlMainParent;

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
    private JFXSlider sldMusic;

    @FXML
    private Pane pnlNowPlaying;

    @FXML
    private Pane nowPlayingBar;

    @FXML
    public void initialize()
    {
        try {
            //pnlSearch = FXMLLoader.load(getClass().getResource("../Views/PanelSearch.fxml"));
            //pnlMainParent.getChildren().set(0,pnlSearch);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../Views/PanelSearch.fxml"));
            pnlSearch = (Pane) loader.load();
            searchController = loader.getController();
            pnlMainParent.getChildren().set(0,pnlSearch);
            FXMLLoader loaderQueue = new FXMLLoader();
            loaderQueue.setLocation(getClass().getResource("../Views/PanelQueue.fxml"));
            pnlQueue = (Pane) loaderQueue.load();
            panelQueueController = loaderQueue.getController();
            pnlQueue.setLayoutY(565);
            pnlQueueSearchParent.getChildren().setAll( pnlQueue);


            FXMLLoader loaderHome = new FXMLLoader();
            loaderHome.setLocation(getClass().getResource("../Views/PanelHome.fxml"));
            pnlHome = (Pane) loaderHome.load();
            if(panelQueueController != null){
                panelQueueController.setHomeController(loaderHome.getController());
            }
            FXMLLoader loaderPlaylist = new FXMLLoader();
            loaderPlaylist.setLocation(getClass().getResource("../Views/PanelPlaylist.fxml"));
            pnlPlaylist = loaderPlaylist.load();
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
            //Pane now playing
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../Views/PanelNowPlaying.fxml"));
            //nowPlayingBar = FXMLLoader.load();
            nowPlayingBar = loader.load();
            nowPlayingController = loader.getController();
            nowPlayingController.setParentController(this);
            nowPlayingController.setPanelPlaylistController(loaderPlaylist.getController(), loaderQueue.getController());
            pnlNowPlaying.getChildren().setAll(nowPlayingBar);
            //
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }

        tbxSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                if (oldValue.isEmpty()) {
                    if (isDropDown){
                        dropDownTimerPanel();
                    }
                    if (isDropDownQueue){
                        dropDownQueuePanelForSearch();
                    }
                    pnlSearch.setVisible(true);
                    pnlSearch.toFront();
                    return;
                }
                if (newValue.isEmpty()){
                    pnlSearch.setVisible(false);
                    pnlSearch.toBack();
                    searchController.cancelSearch();
                    switchMainPanel(selectedButtonId);
                }
            }
        });
        tbxSearch.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (!tbxSearch.getText().isEmpty())
                {
                    if (isDropDown){
                        dropDownTimerPanel();
                    }
                    if (isDropDownQueue){
                        dropDownQueuePanelForSearch();
                    }
                    pnlSearch.setVisible(true);
                    pnlSearch.toFront();
                }
            }
        });
    }

    public void tbxSearch_KeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER && !tbxSearch.getText().isEmpty()){
            if (isConnectedToInternet()){
                try{
                    searchController.search(tbxSearch.getText());
                }
                catch (IOException e){
                    System.out.print(e.toString());
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Search failed");
                alert.setHeaderText(null);
                alert.setContentText("No internet !!!");

                alert.showAndWait();
            }
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

    public void btnStopTimer_Clicked(ActionEvent actionEvent)
    {
        sTimer.cancel();
        btnTimer.setVisible(true);
        btnStopTimer.setVisible(false);
    }

    public void btnTimer10m_Clicked (ActionEvent actionEvent)
    {
        if (sTimer!=null)
            sTimer.cancel();
        else {
                sTimer = new Timer();
                sTimer.schedule(exitTask, 600000);
            }
        dropDownTimerPanel();
        btnStopTimer.setVisible(true);
        btnTimer.setVisible(false);
        JOptionPane.showMessageDialog(null,"Ứng dụng sẽ tắt sau 10 phút","Hẹn giờ tắt",JOptionPane.CLOSED_OPTION);
    }

    public void btnTimer15m_Clicked (ActionEvent actionEvent)
    {
        if (sTimer!=null)
            sTimer.cancel();
        else
        {
            sTimer = new Timer();
            sTimer.schedule(exitTask, 900000);
        }
        dropDownTimerPanel();
        btnStopTimer.setVisible(true);
        btnTimer.setVisible(false);
        JOptionPane.showMessageDialog(null,"Ứng dụng sẽ tắt sau 15 phút","Hẹn giờ tắt",JOptionPane.CLOSED_OPTION);
    }

    public void btnTimer30m_Clicked (ActionEvent actionEvent)
    {
        if (sTimer!=null)
            sTimer.cancel();
        else
        {
            sTimer = new Timer();
            sTimer.schedule(exitTask, 1800000);
        }
        dropDownTimerPanel();
        btnStopTimer.setVisible(true);
        btnTimer.setVisible(false);
        JOptionPane.showMessageDialog(null,"Ứng dụng sẽ tắt sau 30 phút","Hẹn giờ tắt",JOptionPane.CLOSED_OPTION);
    }

    public void btnTimer1h_Clicked (ActionEvent actionEvent)
    {
        if (sTimer!=null)
            sTimer.cancel();
        else
        {
            sTimer = new Timer();
            sTimer.schedule(exitTask, 3600000);
        }
        dropDownTimerPanel();
        btnStopTimer.setVisible(true);
        btnTimer.setVisible(false);
        JOptionPane.showMessageDialog(null,"Ứng dụng sẽ tắt sau 1 tiếng","Hẹn giờ tắt",JOptionPane.CLOSED_OPTION);
    }


    public void btnAboutUs_Clicked(ActionEvent actionEvent) {
        setPnlSelectedButtonPosition(btnAboutUs);
        switchMainPanel(btnAboutUs.getId());
    }

    public void btnQueue_Clicked(ActionEvent actionEvent){
        dropDownQueuePanel();
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
        double tempLayoutY;
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
        if (isDropDown){
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

    private boolean isDropDownQueue = false;
    void dropDownQueuePanel()
    {
        DoubleProperty pnlQueueSearchParentLayoutY = pnlQueueSearchParent.layoutYProperty();
        javafx.animation.KeyValue pnlQueueSearchParentDropDown;
        if (isDropDownQueue){
            pnlQueueSearchParentDropDown = new javafx.animation.KeyValue(pnlQueueSearchParentLayoutY, 0);
            Timeline tl = new Timeline(new KeyFrame(Duration.seconds(0.25), pnlQueueSearchParentDropDown));
            tl.play();
            tl.onFinishedProperty().set(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    pnlMain.toFront();
                }
            });
            pnlSelectedButton.setVisible(true);
        }
        else {
            pnlQueueSearchParent.toFront();
            pnlQueueSearchParentDropDown = new javafx.animation.KeyValue(pnlQueueSearchParentLayoutY, -565);
            new Timeline(new KeyFrame(Duration.seconds(0.25), pnlQueueSearchParentDropDown)).play();
            pnlSelectedButton.setVisible(false);
        }
        isDropDownQueue = !isDropDownQueue;
    }

    void dropDownQueuePanelForSearch()
    {
        DoubleProperty pnlQueueSearchParentLayoutY = pnlQueueSearchParent.layoutYProperty();
        javafx.animation.KeyValue pnlQueueSearchParentDropDown;
        if (isDropDownQueue){
            pnlQueueSearchParentDropDown = new javafx.animation.KeyValue(pnlQueueSearchParentLayoutY, 0);
            Timeline tl = new Timeline(new KeyFrame(Duration.seconds(0.5), pnlQueueSearchParentDropDown));
            tl.play();
            tl.onFinishedProperty().set(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    pnlSearch.toFront();
                }
            });
            pnlSelectedButton.setVisible(true);
        }
        else {
            pnlQueueSearchParent.toFront();
            pnlQueueSearchParentDropDown = new javafx.animation.KeyValue(pnlQueueSearchParentLayoutY, -565);
            new Timeline(new KeyFrame(Duration.seconds(0.5), pnlQueueSearchParentDropDown)).play();
            pnlSelectedButton.setVisible(false);
        }
        isDropDownQueue = !isDropDownQueue;
    }

    void switchMainPanel(String btnId){
        pnlLogo.setVisible(true);
        if (isDropDown){
            dropDownTimerPanel();
        }
        if (isDropDownQueue){
            dropDownQueuePanel();
        }

        pnlSearch.setVisible(false);
        pnlSearch.toBack();

        DoubleProperty pnlMainLayoutY = pnlMain.layoutYProperty();
        javafx.animation.KeyValue pnlMainDropDown;
        switch (btnId) {
            case "btnHome":
                pnlMainDropDown = new KeyValue(pnlMainLayoutY, 0);
                new Timeline(new KeyFrame(Duration.seconds(0.5), pnlMainDropDown)).play();

                selectedButtonId = btnId;
                break;
            case "btnPlaylist":
                pnlMainDropDown = new KeyValue(pnlMainLayoutY, - 565);
                new Timeline(new KeyFrame(Duration.seconds(0.5), pnlMainDropDown)).play();

                selectedButtonId = btnId;
                break;
            case "btnCutter":
                pnlMainDropDown = new KeyValue(pnlMainLayoutY, - 565*2);
                new Timeline(new KeyFrame(Duration.seconds(0.5), pnlMainDropDown)).play();

                selectedButtonId = btnId;
                break;
            case "btnConverter":
                pnlMainDropDown = new KeyValue(pnlMainLayoutY, - 565*3);
                new Timeline(new KeyFrame(Duration.seconds(0.5), pnlMainDropDown)).play();

                selectedButtonId = btnId;
                break;
            case "btnAboutUs":
                pnlMainDropDown = new KeyValue(pnlMainLayoutY, - 565*4);
                new Timeline(new KeyFrame(Duration.seconds(0.5), pnlMainDropDown)).play();

                pnlLogo.setVisible(false);

                selectedButtonId = btnId;
                break;
        }
    }

    
}
