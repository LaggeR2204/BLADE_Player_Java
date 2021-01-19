package sample.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Editor;
import sample.Model.Song;
import sample.helper.Helper;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

import static sample.helper.Helper.formattedTime;


public class PanelCutterController {
    @FXML
    private Label lblName;
    @FXML
    private Label lblArtist;
    @FXML
    private Label lblCategory;
    @FXML
    private Label lblTextName;
    @FXML
    private Label lblTextArtist;
    @FXML
    private Label lblTextGenre;
    @FXML
    private ImageView ivAlbumImg;
    @FXML
    private TextField bMin;
    @FXML
    private TextField bSec;
    @FXML
    private TextField eMin;
    @FXML
    private TextField eSec;
    @FXML
    private Label lblDuration;
    private File iFile, oFile;
    private Song song;
    int duration,beginTime, endTime;
    public void btnAddFile_Clicked(ActionEvent actionEvent)
    {
        Node node = (Node) actionEvent.getSource();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Audio files", "*.wav", "*.mp3");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog((Stage) node.getScene().getWindow());
        if (file != null) {
            iFile = file;
            if (Editor.getExtension(iFile).equals("mp3")) {
                song = new Song(file);
                lblTextName.setVisible(true);
                lblTextArtist.setVisible(true);
                lblTextGenre.setVisible(true);
                lblName.setText(song.getSongName());
                lblArtist.setText(song.getSinger());
                lblCategory.setText(song.getGenre());
                ivAlbumImg.setImage(song.getSongImage());
                duration = song.getDuration();
                lblDuration.setText(formattedTime(duration));
                bMin.setText("0");
                bSec.setText("00");
                eMin.setText(String.valueOf(duration/60));
                eSec.setText(String.valueOf(duration%60));
                beginTime = 0;
                endTime = Integer.parseInt(eMin.getText())*60 + Integer.parseInt(eSec.getText());
            }
            else {
                try {
                    duration = Editor.getWavDuration(iFile);
                    lblDuration.setText(formattedTime(duration));
                    bMin.setText("0");
                    bSec.setText("00");
                    eMin.setText(String.valueOf(duration/60));
                    eSec.setText(String.valueOf(duration%60));
                    lblName.setText(file.getName());
                    beginTime = 0;
                    endTime = Integer.parseInt(eMin.getText())*60 + Integer.parseInt(eSec.getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            eSec.setDisable(false);
            eMin.setDisable(false);
            bSec.setDisable(false);
            bMin.setDisable(false);
        }
    }
    public void tfeSecPressed ()
    {
        if (!eSec.getText().equals(""))
            eSec.clear();
    }
    public void tfbSecPressed ()
    {
        if (!bSec.getText().equals(""))
            bSec.clear();
    }
    public void tfeMinPressed ()
    {
        if (!eMin.getText().equals(""))
            eMin.clear();
    }
    public void tfbMinPressed ()
    {
        if (!bMin.getText().equals(""))
            bMin.clear();
    }
    public void tfeSecInput ()
    {
        if (!eSec.getText().equals(""))
        if (Integer.parseInt(eSec.getText())>60 || (Integer.parseInt(eMin.getText())*60 + Integer.parseInt(eSec.getText()))>endTime)
            eSec.setText(String.valueOf(duration%60));
        if ((Integer.parseInt(bMin.getText())*60 + Integer.parseInt(bSec.getText()))>(Integer.parseInt(eMin.getText())*60 + Integer.parseInt(eSec.getText())))
        {
            bMin.setText(eMin.getText());
            bSec.setText(eSec.getText());
        }
    }
    public void tfbSecInput ()
    {
        if (!bSec.getText().equals(""))
        if (Integer.parseInt(bSec.getText())>60 || (Integer.parseInt(bMin.getText())*60 + Integer.parseInt(bSec.getText()))>(Integer.parseInt(eMin.getText())*60 + Integer.parseInt(eSec.getText()))) {
            bSec.setText(eSec.getText());
        }
    }
    public void tfeMinInput ()
    {
        if (!eMin.getText().equals(""))
        if (Integer.parseInt(eMin.getText())>60 || (Integer.parseInt(eMin.getText())*60 + Integer.parseInt(eSec.getText()))>endTime)
            eMin.setText(String.valueOf(duration/60));
        if ((Integer.parseInt(bMin.getText())*60 + Integer.parseInt(bSec.getText()))>(Integer.parseInt(eMin.getText())*60 + Integer.parseInt(eSec.getText())))
        {
            eMin.setText(eMin.getText());
            bSec.setText(eSec.getText());
        }
    }
    public void tfbMinInput ()
    {
        if (!bMin.getText().equals(""))
        if (Integer.parseInt(bMin.getText())>60 || (Integer.parseInt(bMin.getText())*60 + Integer.parseInt(bSec.getText()))>(Integer.parseInt(eMin.getText())*60 + Integer.parseInt(eSec.getText()))) {
            bMin.setText(eMin.getText());
            bSec.setText(eSec.getText());
        }
    }
    public void btnSave_Clicked(ActionEvent actionEvent)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (iFile!=null) {
            Node node = (Node) actionEvent.getSource();
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("WAV files (*.wav)", "*.wav");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialFileName(iFile.getName().replace(" ", "-") + "-Trimmed");
            File file = fileChooser.showSaveDialog((Stage) node.getScene().getWindow());
            oFile = file;
            File wavFile = new File("proto.wav");
            beginTime = Integer.parseInt(bMin.getText()) * 60 + Integer.parseInt(bSec.getText());
            endTime = Integer.parseInt(eMin.getText()) * 60 + Integer.parseInt(eSec.getText());
            int duration = endTime - beginTime;
            try {
                if (song != null) {
                    Editor.mp3ToWav(iFile, wavFile.getPath());
                    Editor.copyAudio(wavFile, file.getAbsolutePath(), beginTime, duration);
                } else
                    Editor.copyAudio(iFile, file.getAbsolutePath(), beginTime, duration);
            } catch (Exception e) {
                e.printStackTrace();
            }
            alert.setTitle("Cutter");
            alert.setHeaderText(null);
            alert.setContentText("Complete!");
            alert.showAndWait();
        }
        else
        {
            alert.setTitle("Cutter");
            alert.setHeaderText(null);
            alert.setContentText("Please select a file first!");
            alert.showAndWait();
        }
    }
}
