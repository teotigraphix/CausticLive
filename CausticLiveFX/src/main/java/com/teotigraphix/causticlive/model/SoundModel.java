
package com.teotigraphix.causticlive.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustic.model.ICaustkModelState;
import com.teotigraphix.caustic.model.ModelBase;
import com.teotigraphix.causticlive.mediator.SongMediator;
import com.teotigraphix.causticlive.model.IPadModel.PadDataState;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.CtkDebug;
import com.teotigraphix.caustk.core.components.SynthComponent;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.library.LibraryPatch;
import com.teotigraphix.caustk.library.LibraryScene;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.SequencerMode;
import com.teotigraphix.caustk.sequencer.Track;
import com.teotigraphix.caustk.sequencer.TrackItem;
import com.teotigraphix.caustk.sequencer.TrackSong;
import com.teotigraphix.caustk.tone.Tone;

@Singleton
public class SoundModel extends ModelBase implements ISoundModel {

    @Inject
    IPadMapModel padMapModel;

    private List<PadData> queue = new ArrayList<PadData>();

    public void beatChange(int measure, int beat) {
        getSong().nextBeat();

        //CtkDebug.model(">> Beat:" + measure + ", " + beat);

        int isNewMeasure = beat % 4;

        lockAndExtendPlayingTracks();

        if (isNewMeasure == 0) {
            updateTracks();
        }
    }

    private void updateTracks() {
        CtkDebug.log("Setup queued ");
        final int currentMeasure = getSong().getCurrentMeasure();
        // loop through the queue and add queued items
        for (PadData data : queue) {
            if (data.getState() == PadDataState.QUEUED) {
                // add to sequencer
                addChannelTracks(data, getSong(), currentMeasure);
                data.setState(PadDataState.SELECTED);
            } else if (data.getState() == PadDataState.SELECTED) {

            }
        }
    }

    private void lockAndExtendPlayingTracks() {
        final int beat = getSong().getCurrentBeat();
        final int currentMeasure = getSong().getCurrentMeasure();
        final int isNewMeasure = beat % 4;

        // from here on, we have everything correct with current beat and measure
        // the TrackSong's cursor is correct.
        // Check to see if there are any tracks that are in their last beat
        // All tracks in the last beat get extended their length

        for (Track track : getSong().getTracks()) {

            // First try an find a track item at the current measure
            List<TrackItem> list = track.getItemsOnMeasure(getCurrentMeasure());
            for (TrackItem item : list) {
                //String name = PatternUtils.toString(item.getBankIndex(), item.getPatternIndex());

                int numPhraseMeasures = item.getNumMeasures();
                int numBeatsInPhrase = (4 * numPhraseMeasures);
                int startMeasure = beat % numBeatsInPhrase;

                //CtkDebug.model("XXX:" + startMeasure);
                if (isNewMeasure == 0) {

                } else if (startMeasure == numBeatsInPhrase - 1) {
                    // one beat before changing measures
                    // signal add crap, lock UI for patterns in their last beat
                    // 1 measure, 3 beat, 2 measure 7 beat, 4 measure, 15 beat 8, 31
                    PadData data = padMapModel.getPad(item.getBankIndex(), item.getPatternIndex());

                    for (PadChannel channel : data.getChannels()) {
                        if (data.getState() == PadDataState.SELECTED) {
                            try {
                                // add the phrase at the very next measure
                                track.addPhraseAt(currentMeasure + 1, 1, channel.getChannelPhrase());
                            } catch (CausticException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else if (data.getState() == PadDataState.UNQUEUED) {
                            // remove the item from the que
                            queue.remove(data);
                            data.setState(PadDataState.IDLE);
                        }
                    }

                    //CtkDebug.model("Lock:" + beat);
                }
            }
        }
    }

    public static void addChannelTracks(PadData data, TrackSong song, int currentMeasure) {
        for (PadChannel channel : data.getChannels()) {
            Track track = song.getTrack(channel.getIndex());
            try {
                track.addPhraseAt(currentMeasure, 1, channel.getChannelPhrase());
            } catch (CausticException e) {
                e.printStackTrace();
            }
        }
    }

    public void measureChange(int measure) {
        //CtkDebug.model(">>>>>> Measure:" + measure);
    }

    @Override
    public void play() throws CausticException {
        getSong().rewind();

        for (PadData data : queue) {
            if (data.getState() == PadDataState.QUEUED) {
                // add to sequencer
                for (PadChannel channel : data.getChannels()) {
                    Track track = getSong().getTrack(channel.getIndex());
                    track.addPhrase(1, channel.getChannelPhrase());
                }
                data.setState(PadDataState.SELECTED);
            } else if (data.getState() == PadDataState.SELECTED) {

            }
        }
        //getSong().play();
        getController().getSystemSequencer().play(SequencerMode.SONG);
    }

    @Override
    public void stop() {
        getController().getSystemSequencer().stop();
        getController().getSongSequencer().playPosition(0);
    }

    @Override
    public void queue(PadData data) {
        if (!queue.contains(data)) {
            CtkDebug.log("Queue:" + data);
            data.setState(PadDataState.QUEUED);
            queue.add(data);
        }
    }

    @Override
    public void unqueue(PadData data) {
        if (queue.contains(data)) {
            if (data.getState() == PadDataState.QUEUED) {
                data.setState(PadDataState.IDLE);
                queue.remove(data);
            } else {
                data.setState(PadDataState.UNQUEUED);
            }
        }
    }

    @Inject
    SongMediator songMediator;

    @Inject
    IPadModel padModel;

    @Override
    protected SoundModelState getState() {
        return (SoundModelState)super.getState();
    }

    public TrackSong getSong() {
        return getController().getSongManager().getTrackSong();
    }

    //--------------------------------------------------------------------------
    // Property API
    //--------------------------------------------------------------------------

    @Override
    public int getCurrentMeasure() {
        return getSong().getCurrentMeasure();
    }

    @Override
    public int getCurrentBeat() {
        return getSong().getCurrentBeat();
    }

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
    protected void initalizeState() {
        File file = getState().getSelectedSong();
        if (file == null) {
            file = getSong().getFile();
            CtkDebug.model("Load song: " + file.getPath());
            getState().setSelectedSong(file);
        }
        // init song stuff
        try {
            getController().getSongManager().load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        TrackSong song = getSong();
        song.setNumTracks(14);
    }

    @Override
    public void onRegister() {
    }

    @Override
    public void onShow() {

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

        @Inject
        private transient ISoundModel soundModel;

        private Map<Integer, ToneData> toneMap = new HashMap<Integer, ToneData>();

        public List<ToneData> getTones() {
            return new ArrayList<>(toneMap.values());
        }

        private File selectedSong;

        public File getSelectedSong() {
            return selectedSong;
        }

        public void setSelectedSong(File valeu) {
            selectedSong = valeu;
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

        @Override
        public void sleep() {
        }

        @Override
        public void wakeup(ICaustkController controller) {
            File file = getSelectedLibrary();
            if (file != null) {
                soundModel.loadLibrary(file);
            }

            //        // XXX get rid of this
            //        Integer index = getController().getProjectManager().getProject()
            //                .getInteger("assignmentIndex");
            //        if (index == null)
            //            index = 0;
            //
            //        padModel.setAssignmentIndex(index);

            UUID sceneId = getSelectedScene();
            if (sceneId != null) {
                Library library = controller.getLibraryManager().getSelectedLibrary();
                if (library != null) {
                    LibraryScene scene = library.findSceneById(sceneId);
                    if (scene != null) {
                        soundModel.loadScene(scene, false);
                    }
                }
            }

            // load patches
            ((SoundModel)soundModel).loadPatches();
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
