package sample.Model;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import javafx.scene.image.Image;
import sample.helper.Helper;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class Song extends BaseModel {

    //region Properties
    private String SongName;
    private String SongPath;
    private String Singer;
    private String Genre;
    private String Album;
    //transient private Image SongImage;
    private byte[] Image;
    private boolean IsFavorite;
    private String SongURL;
    private int SongNumber;
    private byte[] data;
    private int Duration;

    //endregion

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }

    //region Get_Set_Properties
    public byte[] getData() {
        return data;
    }

    public String getSongName() {
        return SongName;
    }

    public void setSongName(String songName) {
        SongName = songName;
    }

    public String getSongPath() {
        return SongPath;
    }

    public void setSongPath(String songPath) {
        SongPath = songPath;
    }

    public String getSinger() {
        return Singer;
    }

    public void setSinger(String singer) {
        Singer = singer;
    }

    public String getAlbum() {
        return Album;
    }

    public void setAlbum(String album) {
        Album = album;
    }

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public Image getSongImage() {
        if(Image != null)
            return Helper.getImage(this.Image);
        return null;
    }

//    public void setSongImage(Image songImage) {
//        SongImage = songImage;
//    }
    public void setSongImage(byte[] songImage) {
        Image = songImage;
    }

    public boolean isFavorite() {
        return IsFavorite;
    }

    public void setFavorite(boolean favorite) {
        IsFavorite = favorite;
    }

    public String getSongURL() {
        return SongURL;
    }

    public void setSongURL(String songURL) {
        SongURL = songURL;
    }

    public int getSongNumber() {
        return SongNumber;
    }

    public void setSongNumber(int songNumber) {
        SongNumber = songNumber;
    }
    //endregion

    //region Constructor
    public Song() {
        SongName = "";
        SongPath = "";
        Singer = "";
        Genre = "";
        Album = "";
        Image = null;
        IsFavorite = false;
        SongURL = "";
        SongNumber = -1;
        data = null;
        Duration = 0;
    }

    public Song(File file) throws IOException, UnsupportedAudioFileException {
        if(Helper.getFileExtension(file).equals("mp3"))
        {
            try {
                setSongPath(file.getPath());
                Mp3File mp3File = new Mp3File(file);
                ID3v2 id3v2Tag = mp3File.getId3v2Tag();
                setSongName(id3v2Tag.getTitle());
                setSinger(id3v2Tag.getArtist());
                setAlbum(id3v2Tag.getAlbum());
                setGenre(id3v2Tag.getGenreDescription());
                setSongImage(id3v2Tag.getAlbumImage());
                data = Helper.readMP3AudioFileData(file);
                Duration = data.length / 4 / 44100;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            setSongPath(file.getPath());
            loadData();
        }
    }

    public void loadData() throws IOException, UnsupportedAudioFileException {
        byte[] temp = Helper.readWAVAudioFileData(this.SongPath);
        Duration = temp.length / 4 / 44100;
        data = temp;
    }

    public boolean existedSong(Song song) {
        if(this.getSongName().equals(song.getSongName()) && this.getSongPath().equals(song.getSongPath())
        && this.getAlbum().equals(song.getAlbum()) && this.getGenre().equals(song.getAlbum())
        && this.getSinger().equals(song.getSinger()) && this.getDuration() == song.getDuration()) {
            return true;
        }
        return false;
    }
    //endregion
}