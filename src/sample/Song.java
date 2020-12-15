package sample;

import javafx.scene.image.Image;

public class Song {

    //region Properties
    private String SongName;
    private String SongPath;
    private String Singer;
    private String Genre;
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

    public String getGenre() {
        return Genre;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public Image getSongImage() {
        return SongImage;
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
    //endregion
}
