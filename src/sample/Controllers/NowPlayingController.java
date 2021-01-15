package sample.Controllers;

import com.jfoenix.controls.JFXSlider;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import sample.Model.AudioPlayer;
import sample.Model.AudioQueue;
import sample.Model.Song;
import sample.audioInterface.INowSongChangeListener;
import sample.audioInterface.IStatusChangeListener;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

import static sample.helper.Helper.formattedTime;

public class NowPlayingController implements INowSongChangeListener, IStatusChangeListener {
    private MainWindowController parent;
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
        try {
            audioQueue = AudioQueue.getInstance();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
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
        sldVolume.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldNum, Number newNum) {
                audioPlayer.setVolume(newNum.floatValue() / 100);
            }
        });
        audioQueue.addNowSongChangeListener(this);
        audioPlayer.addStatusChangeListener(this);
    }

    public void setParentController(final MainWindowController parent) {
        this.parent = parent;
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
        } else if (audioPlayer.getStatus() == AudioPlayer.STATUS_PAUSE) {
            audioPlayer.play();
        } else {
            Song song = null;
            try {
                song = audioQueue.nextSong();
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
            if (song != null) {
                setDisplayData(song);
                }

        }
    }

    public void setBtnNextSong_Clicked(ActionEvent actionEvent) {
        Song song = null;
        try {
            song = audioQueue.nextSong();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        if(song != null)
            {
                setDisplayData(song);
            }

    }

    public void setBtnVolume_Clicked(ActionEvent actionEvent) {

    }

    public void setBtnPreSong_Clicked(ActionEvent actionEvent) {
        Song song = null;
        try {
            song = audioQueue.preSong();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        if(song != null)
            {
                setDisplayData(song);
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
            sldVolume.setMax(100);
            sldVolume.setMin(0);
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

    public void setBtnShuffle_Clicked(ActionEvent actionEvent)
    {
        audioQueue.setShuffle();
    }

    public void setBtnQueue_Clicked(ActionEvent actionEvent) {
        this.parent.dropDownQueuePanel();
    }
    @Override
    public void onNowSongChangeListener(Object sender, Song newSong) {
        setDisplayData(newSong);
    }

    @Override
    public void onStatusChangeListener(Object sender, int newStatus) {
        if(newStatus == AudioPlayer.STATUS_PLAY)
        {
            audioPlayer.setVolume((float)(sldVolume.getValue() / 100.0f));
            createPlayThread();
        }
        else
        {
            playingThread.stop();
        }

        System.out.println("status" + newStatus);
    }
}
