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
import sample.helper.Helper;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

import static sample.helper.Helper.formattedTime;

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
        setDisplayData(null);
    }

    private void initEventListener() {
        sldMusic.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldNum, Number newNum) {
                if (newNum.intValue() - oldNum.intValue() > 1 || newNum.intValue() < oldNum.intValue()) {
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
        } else {
            try {
                Song song = audioQueue.nextSong();
                if(song != null)
                {
                    audioPlayer.changeAudio(song);
                    createPlayThread();
                }
                setDisplayData(song);
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
            Song song = audioQueue.nextSong();
            if(song != null)
            {
                audioPlayer.changeAudio(song);
                createPlayThread();
            }
            setDisplayData(song);
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
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3 files (*.mp3)", "*.mp3"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("WAV files (*.wav)", "*.wav"));
        File file = fileChooser.showOpenDialog((Stage) node.getScene().getWindow());
        Thread thr = new Thread() {
            @Override
            public void run() {
                super.run();
                Song song;
                if (Helper.getFileExtension(file).equals("mp3")) {
                    song = new Song(file);
                } else {
                    song = new Song();
                    song.setSongPath(file.getPath());
                    try {
                        song.loadData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                }
                audioQueue.addQueue(song);
            }
        };
        thr.start();
    }

    public void setBtnPreSong_Clicked(ActionEvent actionEvent) {
        try {
            Song song = audioQueue.preSong();
            if(song != null)
            {
                audioPlayer.changeAudio(song);
                createPlayThread();
            }
            setDisplayData(song);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void setDisplayData(Song song) {
        if(song == null)
        {
            lbArtist.setText("...");
            lbSongName.setText("...");
            imvCoverArt.setImage(null);
            lbCurrentDuration.setText("...");
            lbTotalDuration.setText("...");
            sldMusic.setDisable(true);
            return;
        }
        lbArtist.setText(song.getSinger());
        lbSongName.setText(song.getSongName());
        imvCoverArt.setImage(song.getSongImage());
        String cur = formattedTime(0);
        int totalDuration = audioPlayer.getTotalSecondDuration();
        String total = formattedTime(totalDuration);
        lbCurrentDuration.setText(cur);
        lbTotalDuration.setText(total);
        sldMusic.setMax(totalDuration);
        sldMusic.setValue(0);
        sldMusic.setDisable(false);
    }


    @Override
    public void onNowSongChangeListener(Object sender, Song newSong) {
        setDisplayData(newSong);
    }
}
