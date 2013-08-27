
package com.teotigraphix.causticlive.model;

import com.teotigraphix.causticlive.model.SoundModel.ToneData;
import com.teotigraphix.causticlive.model.vo.PadData;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.library.LibraryPatch;
import com.teotigraphix.caustk.library.LibraryScene;
import com.teotigraphix.caustk.sequencer.TrackSong;
import com.teotigraphix.libgdx.model.ICaustkModel;

public interface ISoundModel extends ICaustkModel {

    //----------------------------------
    // library
    //----------------------------------

    Library getLibrary();

    //----------------------------------
    // tone
    //----------------------------------

    int getSelectedTone();

    ToneData getSelectedToneData();

    void loadScene(LibraryScene libraryScene, boolean reset);

    void setSelectedTone(int value);

    boolean setPatch(ToneData data, LibraryPatch patch);

    LibraryScene getLibraryScene();

    //    void loadLibrary(File file);

    void play() throws CausticException;

    void stop();

    void queue(PadData data);

    void unqueue(PadData data);

    //    int getCurrentMeasure();
    //
    //    int getCurrentBeat();

    TrackSong getSong();

    //void beatChange(int measure, int beat);

    /**
     * Triggered every beat change after the model has been updated.
     */
    public static class OnSoundModelRefresh {

        private int beat;

        public int getBeat() {
            return beat;
        }

        public OnSoundModelRefresh(int beat) {
            this.beat = beat;
        }
    }

    public static class OnSoundModelLibraryImportComplete {
    }

    void setCurrentBeat(float beat);

}
