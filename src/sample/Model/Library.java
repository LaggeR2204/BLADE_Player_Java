package sample.Model;

import java.util.Date;
import java.util.List;

public class Library {

    private static Library INSTANCE = null;
    List<Playlist> ListPL;
    Date CreateDate;

    private Library() {

    }

    public static Library getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Library();
        return INSTANCE;
    }

    public Date getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(Date createDate) {
        CreateDate = createDate;
    }

    public List<Playlist> getListPL() {
        return ListPL;
    }

    public void setListPL(List<Playlist> listPL) {
        ListPL = listPL;
    }
}
