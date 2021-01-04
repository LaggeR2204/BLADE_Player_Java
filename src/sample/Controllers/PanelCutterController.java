package sample.Controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Editor;
import sample.Model.Song;

import javax.swing.*;
import java.io.File;

public class PanelCutterController {
    private File iFile, oFile;
    private Song song;
    public void btnA_Clicked(ActionEvent actionEvent)
    {
        Node node = (Node) actionEvent.getSource();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MP3 files (*.mp3)", "*.mp3");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog((Stage) node.getScene().getWindow());
        iFile= file;
        song = new Song(file);
    }
    public void btnB_Clicked(ActionEvent actionEvent)
    {
        Node node = (Node) actionEvent.getSource();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("WAV files (*.wav)", "*.wav");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName(song.getSongName().replace(" ", "-")+"-Trimmed");
        File file = fileChooser.showSaveDialog((Stage) node.getScene().getWindow());
        oFile = file;
        try {
            Editor.copyAudio(iFile,file.getAbsolutePath(),30,30);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
