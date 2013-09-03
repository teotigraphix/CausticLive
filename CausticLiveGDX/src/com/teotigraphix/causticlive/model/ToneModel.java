
package com.teotigraphix.causticlive.model;

import java.io.File;

import com.google.inject.Singleton;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.library.LibraryPatch;
import com.teotigraphix.caustk.sequencer.queue.QueueData;
import com.teotigraphix.caustk.tone.Tone;
import com.teotigraphix.libgdx.model.ModelBase;

@Singleton
public class ToneModel extends ModelBase implements IToneModel {

    @Override
    public void assignTone(int toneIndex, QueueData queueData) {
        queueData.setViewChannel(toneIndex);
    }

    @Override
    public void assignPatch(QueueData data, LibraryPatch libraryPatch) {
        int toneIndex = data.getViewChannel();
        Library library = getController().getLibraryManager().getSelectedLibrary();
        File presetFile = library.getPresetFile(libraryPatch.getPresetFile());
        Tone tone = getController().getSoundSource().getTone(toneIndex);
        tone.getSynth().loadPreset(presetFile.getAbsolutePath());
    }

    public ToneModel() {
    }

    @Override
    public void onRegister() {
    }

    @Override
    public void onShow() {
    }

}
