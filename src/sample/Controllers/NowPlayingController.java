package sample.Controllers;

import com.jfoenix.controls.JFXSlider;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.audioInterface.INowSongChangeListener;
import sample.Model.AudioPlayer;
import sample.Model.AudioQueue;
import sample.Model.Song;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class NowPlayingController implements INowSongChangeListener {
    @FXML
    private ImageView imvCoverArt;

    @FXML
    private Label lbSongName;

    @FXML
    private Label lbArtist;

    @FXML
    private JFXSlider sldVolume;

    @FXML
    private Button btnPlayPause;

    @FXML
    private Button btnPreSong;

    @FXML
    private Button btnShuffle;

    @FXML
    private Button btnNextSong;

    @FXML
    private Button btnQueue;

    @FXML
    private Button btnVolume;

    @FXML
    private Label lbCurrentDuration;

    @FXML
    private Label lbTotalDuration;

    @FXML
    private JFXSlider sldMusic;

    @FXML
    private void initialize() {
        audioQueue = AudioQueue.getInstance();
        try {
            audioPlayer = AudioPlayer.getInstance();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        initEventListener();
//        Song song0 = new Song();
//        song0.setSongPath("D:\\Music\\US-UK\\Lemon Tree - Fools Garden.mp3");
//        Song song = new Song();
//        song.setSongPath("D:\\Music\\US-UK\\Lemon Tree - Fools Garden.wav");
//        Song song1 = new Song();
//        song1.setSongPath("D:\\Music\\US-UK\\On My Way - Alan Walker_ Sabrina Carpent.wav");
//        Song song2 = new Song();
//        song2.setSongPath("D:\\Music\\US-UK\\Can We Kiss Forever_ - Kina_ Adriana Pro.wav");
//        //audioQueue.addQueue(song0);
//        audioQueue.addQueue(song);
//        audioQueue.addQueue(song1);
//        audioQueue.addQueue(song2);
//        try {
//            audioPlayer.changeAudio(audioQueue.nextSong());
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//        } catch (UnsupportedAudioFileException e) {
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//        } catch (LineUnavailableException e) {
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//        }
//        setDisplayData(song);
//        createPlayThread();
    }
    private void initEventListener(){
        sldMusic.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldNum, Number newNum) {
                if(newNum.intValue() - oldNum.intValue() > 1 || newNum.intValue() < oldNum.intValue())
                {
                    try {
                        audioPlayer.jump(newNum.longValue() * 1000000);
                        lbCurrentDuration.setText(formattedTime(newNum.intValue()));
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (LineUnavailableException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        audioQueue.addNowSongChangeListener(this);
    }
    private void createPlayThread() {
        playingThread = new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    Platform.runLater(() -> {
                        int cur = audioPlayer.getCurrentSecondDuration();
                        sldMusic.setValue(cur);
                        lbCurrentDuration.setText(formattedTime(cur));
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        playingThread.start();
    }

    private AudioQueue audioQueue;
    private AudioPlayer audioPlayer;
    private Thread playingThread;

    public void setBtnPlayPause_Clicked(ActionEvent actionEvent) {
        if (audioPlayer.getStatus() == AudioPlayer.STATUS_PLAY) {
            audioPlayer.pause();
            playingThread.stop();
        } else if (audioPlayer.getStatus() == AudioPlayer.STATUS_PAUSE) {
            audioPlayer.play();
            createPlayThread();
        } else{
            try {
                Song song = audioQueue.nextSong();
                audioPlayer.changeAudio(song);
                setDisplayData(song);
                createPlayThread();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }
    }

    public void setBtnNextSong_Clicked(ActionEvent actionEvent) {
        try {
            Song next = audioQueue.nextSong();
            audioPlayer.changeAudio(next);
            setDisplayData(next);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void setBtnVolume_Clicked(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MP3 files (*.mp3)", "*.mp3");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog((Stage) node.getScene().getWindow());
        Song song = new Song(file);
        audioQueue.addQueue(song);
    }

    public void setBtnPreSong_Clicked(ActionEvent actionEvent) {
        try {
            Song next = audioQueue.preSong();
            audioPlayer.changeAudio(next);
            setDisplayData(next);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    private void setDisplayData(Song song) {
        lbArtist.setText(song.getSongPath());
        lbSongName.setText(song.getSongPath());
        String cur = formattedTime(0);
        int totalDuration = audioPlayer.getTotalSecondDuration();
        String total = formattedTime(totalDuration);
        lbCurrentDuration.setText(cur);
        lbTotalDuration.setText(total);
        sldMusic.setMax(totalDuration);
        sldMusic.setValue(0);
    }

    private String formattedTime(int mCurrentPosition) {
        String totalOut;
        String totalNew;
        String seconds = String.valueOf(mCurrentPosition % 60);
        String minutes = String.valueOf(mCurrentPosition / 60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":0" + seconds;
        if (seconds.length() == 1) {
            return totalNew;
        }
        return totalOut;
    }

    @Override
    public void onNowSongChangeListener(Object sender, Song newSong) {
        setDisplayData(newSong);
    }
}
