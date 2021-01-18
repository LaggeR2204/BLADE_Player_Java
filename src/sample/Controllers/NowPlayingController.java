package sample.Controllers;

import com.jfoenix.controls.JFXSlider;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.Model.AudioPlayer;
import sample.Model.AudioQueue;
import sample.Model.Song;
import sample.audioInterface.INowSongChangeListener;
import sample.audioInterface.IStatusChangeListener;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static sample.helper.Helper.formattedTime;

public class NowPlayingController implements INowSongChangeListener, IStatusChangeListener {
    private MainWindowController parent;
    private PanelPlaylistController panelPlaylistController;
    @FXML
    private ImageView imvCoverArt;

    @FXML
    private Label lbSongName;

    @FXML
    private Label lbArtist;

    @FXML
    private JFXSlider sldVolume;

    @FXML
    private ImageView imgPlayPause;

    @FXML
    private ImageView imgShuffle;

    @FXML
    private ImageView imgVolume;

    @FXML
    private Label lbCurrentDuration;

    @FXML
    private Label lbTotalDuration;

    @FXML
    private JFXSlider sldMusic;

    Image imgPlay = null;
    Image imgPause = null;
    Image imgIsShuffle_True = null;
    Image imgIsShuffle_False = null;
    Image imgVolume_True = null;
    Image imgVolume_False = null;

    @FXML
    private void initialize() {

        try {
            imgVolume_True = new Image(new FileInputStream("src/sample/img/voice_30px.png"));
            imgVolume_False = new Image(new FileInputStream("src/sample/img/mute_30px.png"));
            imgIsShuffle_True = new Image(new FileInputStream("src/sample/img/shuffle_30px.png"));
            imgIsShuffle_False = new Image(new FileInputStream("src/sample/img/repeat_30px.png"));
            imgPause = new Image(new FileInputStream("src/sample/img/pause_60px.png"));
            imgPlay = new Image(new FileInputStream("src/sample/img/play_60px.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
                if (newNum.floatValue() / 100 == 0)
                {
                    imgVolume.setImage(imgVolume_False);
                }
                else
                {
                    imgVolume.setImage(imgVolume_True);
                }
            }
        });
        audioQueue.addNowSongChangeListener(this);
        audioPlayer.addStatusChangeListener(this);

        lbSongName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                lbSongName.setTooltip(new Tooltip(lbSongName.getText()));

            }
        });
        lbArtist.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                lbArtist.setTooltip(new Tooltip(lbArtist.getText()));

            }
        });
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
            imgPlayPause.setImage(imgPlay);
            audioPlayer.pause();
        } else if (audioPlayer.getStatus() == AudioPlayer.STATUS_PAUSE) {
            imgPlayPause.setImage(imgPause);
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

    private float lastVolumeValue = 100.0f;
    public void setBtnVolume_Clicked(ActionEvent actionEvent) {
        if (sldVolume.getValue() / 100.0f != 0){
            lastVolumeValue = (float) sldVolume.getValue();
            System.out.println("last volume: " + sldVolume.getValue());
            audioPlayer.setVolume(0);
            sldVolume.setValue(0);
            imgVolume.setImage(imgVolume_False);
        }
        else{
            audioPlayer.setVolume(lastVolumeValue / 100.0f);
            sldVolume.setValue(lastVolumeValue);
            imgVolume.setImage(imgVolume_True);
            System.out.println("last volume: " + sldVolume.getValue());
        }
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
            sldVolume.setValue(100);
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
        if (audioQueue.isShuffle()){
            imgShuffle.setImage(imgIsShuffle_True);
        }
        else{
            imgShuffle.setImage(imgIsShuffle_False);
        }
    }

    public void setBtnQueue_Clicked(ActionEvent actionEvent) {
        this.parent.dropDownQueuePanel();
        panelPlaylistController.resetFavorite();
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
            imgPlayPause.setImage(imgPause);
        }
        else
        {
            playingThread.stop();
            imgPlayPause.setImage(imgPlay);
        }

        System.out.println("status" + newStatus);
    }

    public void setPanelPlaylistController(PanelPlaylistController pnPLCtrl) {
        if (pnPLCtrl != null)
            this.panelPlaylistController = pnPLCtrl;
    }
}
