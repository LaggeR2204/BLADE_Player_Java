package sample.Model;

import java.util.ArrayList;
import java.util.List;

public class Library extends BaseModel {

    private static Library INSTANCE = null;

    List<Playlist> ListPL;

    private Library() {
        Playlist DefaultPL;
        Playlist FavoritePL;
        DefaultPL = new Playlist("Default", false);
        DefaultPL.setDeletable(false);
        FavoritePL = new Playlist("Favorite", false);
        DefaultPL.setDeletable(false);
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

    public void addPlaylistToLibrary(Playlist playlist) {
        this.ListPL.add(playlist);
    }

    public List<Playlist> getListPL() {
        return ListPL;
    }

    public void deletePL(int index) {
        ListPL.remove(index);
    }

    public void setListPL(List<Playlist> listPL) {
        ListPL = listPL;
    }

    public Playlist getDefaultPL() {
        return ListPL.get(0);
    }

    public void setDefaultPL(Playlist defaultPL) {
        if(defaultPL != null)
            ListPL.set(0, defaultPL);
    }

    public Playlist getFavoritePL() {
        return ListPL.get(1);
    }

    public void setFavoritePL(Playlist favoritePL) {
        if (favoritePL != null)
            ListPL.set(1, favoritePL);
    }
}
