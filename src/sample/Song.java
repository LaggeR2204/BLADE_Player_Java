package sample;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import com.mpatric.mp3agic.Mp3File;

public class Song {

    //region Properties
    private String SongName;
    private String SongPath;
    private String Singer;
    private String Genre;
    private String Album;
    private Image SongImage;
    private boolean IsFavorite;
    private String SongURL;
    private int SongNumber;
    //endregion

    //region Get_Set_Properties
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
        return SongImage;
    }
    public Image getImage(byte[] data)
    {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            BufferedImage bImage = ImageIO.read(bis);
            Image songImage = SwingFXUtils.toFXImage(bImage, null );
            return songImage;
        }
        catch (Exception e) {
        e.printStackTrace();}
        return null;
    }

    public void setSongImage(Image songImage) {
        SongImage = songImage;
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

    }

    public Song(File file) {
        try {
            setSongPath(file.getPath());
            Mp3File mp3File = new Mp3File(file);
            ID3v2 id3v2Tag = mp3File.getId3v2Tag();
            setSongName(id3v2Tag.getTitle());
            setSinger(id3v2Tag.getArtist());
            setAlbum(id3v2Tag.getAlbum());
            setGenre(id3v2Tag.getGenreDescription());
            setSongImage(getImage(id3v2Tag.getAlbumImage()));
        }
        catch (Exception e) {
            e.printStackTrace();}
    }

    //endregion
}
