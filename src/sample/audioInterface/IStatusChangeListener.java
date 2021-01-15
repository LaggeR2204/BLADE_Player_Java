package sample.audioInterface;

import sample.Model.Song;

import java.util.Collection;

public interface IStatusChangeListener {
    void onStatusChangeListener(Object sender, int newStatus);
}
