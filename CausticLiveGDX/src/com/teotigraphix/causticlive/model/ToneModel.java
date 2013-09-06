
package com.teotigraphix.causticlive.model;

import java.io.File;
import java.util.UUID;

import com.badlogic.gdx.utils.ArrayMap;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.library.LibraryPatch;
import com.teotigraphix.caustk.sequencer.queue.QueueData;
import com.teotigraphix.caustk.tone.Tone;
import com.teotigraphix.libgdx.model.CaustkModel;
import com.teotigraphix.libgdx.model.ICaustkModelState;

@Singleton
public class ToneModel extends CaustkModel implements IToneModel {

    private int editChannel;

    @Override
    public int getEditChannel() {
        return editChannel;
    }

    @Override
    public void setEditChannel(int value) {
        editChannel = value;
        getDispatcher().trigger(
                new OnToneModelPropertyChange(this, ToneModelPropertyChangeKind.ChannelIndex));
    }

    @Override
    protected ToneModelState getState() {
        return (ToneModelState)super.getState();
    }

    @Override
    public void assignTone(int toneIndex, QueueData queueData) {
        queueData.setViewChannelIndex(toneIndex);
        getDispatcher().trigger(new OnToneModelMachineIndexChange());
    }

    @Override
    public void assignPatch(QueueData data, LibraryPatch libraryPatch) {
        int toneIndex = data.getViewChannelIndex();
        Library library = getController().getLibraryManager().getSelectedLibrary();
        File presetFile = library.getPresetFile(libraryPatch.getPresetFile());
        Tone tone = getController().getSoundSource().getTone(toneIndex);
        tone.getSynth().loadPreset(presetFile.getAbsolutePath());
        getState().putPatchId(toneIndex, libraryPatch.getId());
    }

    @Override
    public UUID getPatchId(int toneIndex) {
        return getState().getPatchId(toneIndex);
    }

    public ToneModel() {
        setStateFactory(ToneModelState.class);
    }

    @Override
    public void onRegister() {
    }

    @Override
    public void onShow() {
    }

    public static class ToneModelState implements ICaustkModelState {

        private ArrayMap<Integer, UUID> patches = new ArrayMap<Integer, UUID>();

        public UUID getPatchId(int toneIndex) {
            return patches.get(toneIndex);
        }

        public void putPatchId(int toneIndex, UUID patchId) {
            patches.put(toneIndex, patchId);
        }

        @SuppressWarnings("unused")
        private transient ICaustkController controller;

        @Override
        public void sleep() {
        }

        @Override
        public void wakeup(ICaustkController controller) {
            this.controller = controller;
        }
    }

}
