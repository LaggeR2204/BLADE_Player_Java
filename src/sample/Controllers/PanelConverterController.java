package sample.Controllers;

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

import java.io.File;

public class PanelConverterController {
    @FXML
    private Label lblName;
    @FXML
    private Label lblArtist;
    @FXML
    private Label lblCategory;
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
        iFile= file;
        song = new Song(file);
        lblName.setText(song.getSongName());
        lblArtist.setText(song.getSinger());
        lblCategory.setText(song.getGenre());
        ivAlbumImg.setImage(song.getSongImage());
        tfFilePath.setText(file.getAbsolutePath());
    }


    public void btnConvert_Clicked(ActionEvent actionEvent)
    {
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
    }

}
