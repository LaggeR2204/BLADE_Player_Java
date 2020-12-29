package sample.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Library {

    private static Library INSTANCE = null;
    Playlist DefaultPL;
    Playlist FavoritePL;
    List<Playlist> ListPL;

    private Library() {
        DefaultPL = new Playlist("Default", false);
        DefaultPL.setDeletable(false);
        FavoritePL = new Playlist("Favorite", false);
        FavoritePL.setDeletable(false);
        ListPL = new ArrayList<>();
        ListPL.add(DefaultPL);
        ListPL.add(FavoritePL);
    }

    public static Library getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Library();
        return INSTANCE;
    }

    public List<Playlist> getListPL() {
        return ListPL;
    }

    public void setListPL(List<Playlist> listPL) {
        ListPL = listPL;
    }

    public Playlist getDefaultPL() {
        return DefaultPL;
    }

    public void setDefaultPL(Playlist defaultPL) {
        DefaultPL = defaultPL;
    }

    public Playlist getFavoritePL() {
        return FavoritePL;
    }

    public void setFavoritePL(Playlist favoritePL) {
        FavoritePL = favoritePL;
    }
}
