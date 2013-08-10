
package com.teotigraphix.causticlive.model;

import java.io.File;

import com.teotigraphix.caustic.model.ICaustkModel;
import com.teotigraphix.causticlive.model.SoundModel.ToneData;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.library.LibraryPatch;
import com.teotigraphix.caustk.library.LibraryScene;

public interface ISoundModel extends ICaustkModel {

    Library getLibrary();

    void loadScene(LibraryScene libraryScene, boolean reset);

    int getSelectedTone();

    ToneData getSelectedToneData();

    void setSelectedTone(int value);

    boolean setPatch(ToneData data, LibraryPatch patch);
    
    public static class OnSoundModelLibraryImportComplete {
        
    }

    LibraryScene getLibraryScene();

    void loadLibrary(File file);


}
