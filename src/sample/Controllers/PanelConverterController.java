package sample.Controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import sample.Editor;
import sample.Model.Song;

import javax.swing.*;
import java.io.File;

public class PanelConverterController {
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
    private TextField tfFilePath;
    private File iFile, oFile;
    private Song song;
    public void btnAddFile_Clicked(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MP3 files (*.mp3)", "*.mp3");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog((Stage) node.getScene().getWindow());
        if (file != null) {
            iFile = file;
            song = new Song(file);
            lblTextName.setVisible(true);
            lblTextArtist.setVisible(true);
            lblTextGenre.setVisible(true);
            lblName.setText(song.getSongName());
            lblArtist.setText(song.getSinger());
            lblCategory.setText(song.getGenre());
            ivAlbumImg.setImage(song.getSongImage());
            tfFilePath.setText(file.getAbsolutePath());
        }
    }


    public void btnConvert_Clicked(ActionEvent actionEvent)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (iFile != null) {
            Node node = (Node) actionEvent.getSource();
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("WAV files (*.wav)", "*.wav");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.setInitialFileName(song.getSongName().replace(" ", "-"));
            File file = fileChooser.showSaveDialog((Stage) node.getScene().getWindow());
            oFile = file;
            try {
                Editor.mp3ToWav(iFile, oFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
            alert.setTitle("Converter");
            alert.setHeaderText(null);
            alert.setContentText("Complete!");
            alert.showAndWait();
        }
        else
        {
            alert.setTitle("Converter");
            alert.setHeaderText(null);
            alert.setContentText("Please select a file first!");
            alert.showAndWait();
        }

    }

}
