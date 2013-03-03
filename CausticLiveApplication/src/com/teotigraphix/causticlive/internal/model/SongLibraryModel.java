
package com.teotigraphix.causticlive.internal.model;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Singleton;
import com.teotigraphix.caustic.song.ISong;
import com.teotigraphix.causticlive.model.ISongLibraryModel;

@Singleton
public class SongLibraryModel implements ISongLibraryModel {

    private List<ISong> songs = new ArrayList<ISong>();

    public void addSong(ISong song) {
        songs.add(song);
    }

    public SongLibraryModel() {

    }

}
