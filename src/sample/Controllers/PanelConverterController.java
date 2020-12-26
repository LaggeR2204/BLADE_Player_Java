package sample.Controllers;

import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import sample.Converter;
import sample.Song;
import com.mpatric.mp3agic.Mp3File;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
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
    public void btnAddFile_Clicked(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MP3 files (*.mp3)", "*.mp3");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog((Stage) node.getScene().getWindow());
        Song song = new Song(file);
        iFile= file;
        lblName.setText(song.getSongName());
        lblArtist.setText(song.getSinger());
        lblCategory.setText(song.getGenre());
        ivAlbumImg.setImage(song.getSongImage());
        tfFilePath.setText(file.getAbsolutePath());
    }
    public void btnConvert_Clicked(ActionEvent actionEvent)
    {
//        Node node = (Node) actionEvent.getSource();
//        FileChooser fileChooser = new FileChooser();
//        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("WAV files (*.wav)", "*.wav");
//        fileChooser.getExtensionFilters().add(extFilter);
//        File file = fileChooser.showSaveDialog((Stage) node.getScene().getWindow());
//        oFile = file;
//        //Converter converter = new Converter(iFile.getAbsolutePath(), oFile.getAbsolutePath());
//
//        try {
//            Mp3File mp3File = new Mp3File(iFile);
//            AudioInputStream AIS = new AudioSystem.getAudioInputStream()
//            int i = AudioSystem.write(,AudioFileFormat.Type.WAVE,oFile);
//            System.out.println("Bytes Written: " + i);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}
