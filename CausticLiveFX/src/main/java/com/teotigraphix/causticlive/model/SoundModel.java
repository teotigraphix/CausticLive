
package com.teotigraphix.causticlive.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustic.model.ICaustkModelState;
import com.teotigraphix.caustic.model.ModelBase;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.components.SynthComponent;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.library.LibraryPatch;
import com.teotigraphix.caustk.library.LibraryScene;
import com.teotigraphix.caustk.tone.Tone;

@Singleton
public class SoundModel extends ModelBase implements ISoundModel {

    @Inject
    IPadModel padModel;

    @Override
    protected SoundModelState getState() {
        return (SoundModelState)super.getState();
    }

    //--------------------------------------------------------------------------
    // Property API
    //--------------------------------------------------------------------------

    //----------------------------------
    // Library
    //----------------------------------

    @Override
    public final Library getLibrary() {
        return getController().getLibraryManager().getSelectedLibrary();
    }

    @Override
    public void loadLibrary(File file) {
        Library library = getController().getLibraryManager().loadLibrary(file);
        if (library != null) {
            getController().getLibraryManager().setSelectedLibrary(library);
            getState().setSelectedLibrary(library.getDirectory());
        }
    }

    //----------------------------------
    // libraryScene
    //----------------------------------

    private LibraryScene libraryScene;

    public LibraryScene getLibraryScene() {
        return libraryScene;
    }

    public void setLibraryScene(LibraryScene libraryScene) {
        this.libraryScene = libraryScene;
        getState().setSelectedScene(libraryScene.getId());
        trigger(new OnSoundModelLibrarySceneChange());

    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public SoundModel() {
        setStateFactory(SoundModelState.class);
    }

    //--------------------------------------------------------------------------
    // Public API
    //--------------------------------------------------------------------------

    //--------------------------------------------------------------------------
    // Public Overrides
    //--------------------------------------------------------------------------

    @Override
    public void onRegister() {
    }

    @Override
    public void onShow() {
        File file = getState().getSelectedLibrary();
        if (file != null) {
            loadLibrary(file);
        }

        //        // XXX get rid of this
        //        Integer index = getController().getProjectManager().getProject()
        //                .getInteger("assignmentIndex");
        //        if (index == null)
        //            index = 0;
        //
        //        padModel.setAssignmentIndex(index);

        UUID sceneId = getState().getSelectedScene();
        if (sceneId != null) {
            Library library = getController().getLibraryManager().getSelectedLibrary();
            if (library != null) {
                LibraryScene scene = library.findSceneById(sceneId);
                if (scene != null) {
                    loadScene(scene, false);
                }
            }
        }

        // load patches
        loadPatches();

        Integer selectedTone = getState().getSelectedTone();
        if (selectedTone != null)
            setSelectedTone(selectedTone);
    }

    private void loadPatches() {
        Library library = getLibrary();
        if (library == null)
            return;

        for (ToneData data : getState().getTones()) {
            UUID patchId = data.getPatchId();
            LibraryPatch libraryPatch = library.findPatchById(patchId);
            if (libraryPatch != null)
                setPatch(data, libraryPatch);
        }

    }

    @Override
    public void loadScene(LibraryScene libraryScene, boolean reset) {
        // Load the scene to create the machines
        getController().getSoundSource().clearAndReset();

        if (reset) {
            getState().clear();
        }
        try {
            getController().getSoundSource().createScene(libraryScene);
        } catch (CausticException e) {
            e.printStackTrace();
        }

        if (reset) {
            for (Tone tone : getController().getSoundSource().getTones()) {
                ToneData data = new ToneData(tone.getIndex());
                getState().putTone(tone.getIndex(), data);
            }
        }

        setLibraryScene(libraryScene);

        //setSelectedTone(-1);
    }

    /**
     * @see SoundModel#loadScene(LibraryScene)
     * @see SoundModel#getDispatcher()
     */
    public static class OnSoundModelLibrarySceneChange {

    }

    private int selectedTone;

    @Override
    public final int getSelectedTone() {
        return selectedTone;
    }

    @Override
    public ToneData getSelectedToneData() {
        return getState().getToneData(selectedTone);
    }

    /**
     * @see OnSoundModelSelectedToneChange
     * @param value
     */
    @Override
    public final void setSelectedTone(int value) {
        // clear the selection
        if (value == -1) {
            selectedTone = -1;
            trigger(new OnSoundModelSelectedToneChange(null, null));
            return;
        }
        if (value == selectedTone)
            return;
        ToneData oldTone = getState().getToneData(selectedTone);
        selectedTone = value;
        ToneData tone = getState().getToneData(selectedTone);
        getState().setSelectedTone(selectedTone);
        trigger(new OnSoundModelSelectedToneChange(tone, oldTone));
    }

    public static class SoundModelState implements ICaustkModelState {

        private Map<Integer, ToneData> toneMap = new HashMap<Integer, ToneData>();

        public List<ToneData> getTones() {
            return new ArrayList<>(toneMap.values());
        }

        private File selectedLibrary;

        public File getSelectedLibrary() {
            return selectedLibrary;
        }

        public void setSelectedLibrary(File selectedLibrary) {
            this.selectedLibrary = selectedLibrary;
        }

        private UUID selectedScene;

        public UUID getSelectedScene() {
            return selectedScene;
        }

        public void putTone(int index, ToneData data) {
            toneMap.put(index, data);
        }

        public void setSelectedScene(UUID selectedScene) {
            this.selectedScene = selectedScene;
        }

        private int selectedTone;

        public int getSelectedTone() {
            return selectedTone;
        }

        public void setSelectedTone(int selectedTone) {
            this.selectedTone = selectedTone;
        }

        /**
         * Clears the tone map.
         */
        public void clear() {
            toneMap.clear();
        }

        public ToneData getToneData(int toneIndex) {
            return toneMap.get(toneIndex);
        }

    }

    public static class ToneData {

        private int toneIndex;

        public final int getToneIndex() {
            return toneIndex;
        }

        private UUID patchId;

        public final UUID getPatchId() {
            return patchId;
        }

        void setPatchId(UUID value) {
            patchId = value;
        }

        public ToneData(int toneIndex) {
            this.toneIndex = toneIndex;
        }

        @Override
        public String toString() {
            return "[" + toneIndex + "]" + patchId;
        }
    }

    public static class OnSoundModelSelectedToneChange {

        private ToneData tone;

        private ToneData oldTone;

        public final ToneData getTone() {
            return tone;
        }

        public final ToneData getOldTone() {
            return oldTone;
        }

        public OnSoundModelSelectedToneChange(ToneData tone, ToneData oldTone) {
            this.tone = tone;
            this.oldTone = oldTone;
        }

    }

    @Override
    public boolean setPatch(ToneData data, LibraryPatch patch) {
        if (data == null)
            return false;

        Tone tone = getController().getSoundSource().getTone(data.getToneIndex());

        if (tone.getToneType() != patch.getToneType())
            return false;

        data.setPatchId(patch.getId());

        SynthComponent component = tone.getComponent(SynthComponent.class);
        File presetFile = patch.getPresetFile();
        File fullPresetFile = getLibrary().getPresetFile(presetFile);
        component.loadPreset(fullPresetFile.getPath());

        return true;
    }

}
