package sample.audioInterface;

import sample.Model.Song;

import java.util.Collection;

public interface IQueueChangeListener {
    void onQueueChangeListener(Object sender, Collection<Song> newQueue);
}
