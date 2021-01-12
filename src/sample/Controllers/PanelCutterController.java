package sample.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
    int duration;
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
    public void btnSave_Clicked(ActionEvent actionEvent)
    {
        Node node = (Node) actionEvent.getSource();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("WAV files (*.wav)", "*.wav");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName(iFile.getName().replace(" ", "-")+"-Trimmed");
        File file = fileChooser.showSaveDialog((Stage) node.getScene().getWindow());
        oFile = file;
        File wavFile = new File("proto.wav");
        int beginTime = Integer.parseInt(bMin.getText())*60 + Integer.parseInt(bSec.getText());
        int endTime = Integer.parseInt(eMin.getText())*60 + Integer.parseInt(eSec.getText());
        int duration = endTime - beginTime;
        try {
            if (song!= null) {
                Editor.mp3ToWav(iFile,wavFile.getPath());
                Editor.copyAudio(wavFile,file.getAbsolutePath(),beginTime,duration);
            }
            else
            Editor.copyAudio(iFile,file.getAbsolutePath(),beginTime,duration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
