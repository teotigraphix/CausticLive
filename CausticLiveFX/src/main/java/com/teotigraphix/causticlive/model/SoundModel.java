
package com.teotigraphix.causticlive.model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.Singleton;
import com.teotigraphix.caustic.model.ModelBase;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.components.SynthComponent;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.library.LibraryPatch;
import com.teotigraphix.caustk.library.LibraryScene;
import com.teotigraphix.caustk.tone.Tone;

@Singleton
public class SoundModel extends ModelBase implements ISoundModel {

    @Override
    public final Library getLibrary() {
        return getController().getLibraryManager().getSelectedLibrary();
    }

    public SoundModel() {
        super();
    }

    @Override
    public void onRegister() {
        Library library = null;
//        try {
//            library = getController().getLibraryManager().createLibrary("Foo");
////            File causticFile = new File(
////                    "C:\\Users\\Work\\Documents\\caustic\\songs\\HARDDESERT1.caustic");
////            getController().getLibraryManager().importSong(library, causticFile);
//            getController().getLibraryManager().setSelectedLibrary(library);
//            getController().getLibraryManager().saveLibrary(library);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        } catch (CausticException e) {
//            e.printStackTrace();
//        }

        //        File causticFile = new File("C:\\Users\\Work\\Documents\\caustic\\songs\\ARPTEST1.caustic");
        //        try {
        //            getController().getSoundSource().loadSong(causticFile);
        //        } catch (CausticException e) {
        //            e.printStackTrace();
        //        }
        //        getController().getSystemSequencer().play(SequencerMode.PATTERN);
    }

    @Override
    public void onShow() {
        // TODO Auto-generated method stub

    }

    @Override
    public void loadScene(LibraryScene libraryScene) {
        // Load the scene to create the machines
        getController().getSoundSource().clearAndReset();

        toneMap.clear();

        try {
            getController().getSoundSource().createScene(libraryScene);
        } catch (CausticException e) {
            e.printStackTrace();
        }

        for (Tone tone : getController().getSoundSource().getTones()) {
            ToneData data = new ToneData(tone);

            toneMap.put(tone.getIndex(), data);
        }

        trigger(new OnSoundModelSceneLoad());

        setSelectedTone(-1);
    }

    /**
     * @see SoundModel#loadScene(LibraryScene)
     * @see SoundModel#getDispatcher()
     */
    public static class OnSoundModelSceneLoad {

    }

    private int selectedTone;

    @Override
    public final int getSelectedTone() {
        return selectedTone;
    }

    @Override
    public ToneData getSelectedToneData() {
        return toneMap.get(selectedTone);
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
        ToneData oldTone = toneMap.get(selectedTone);
        selectedTone = value;
        ToneData tone = toneMap.get(selectedTone);
        trigger(new OnSoundModelSelectedToneChange(tone, oldTone));
    }

    private Map<Integer, ToneData> toneMap = new HashMap<Integer, ToneData>();

    public static class ToneData {

        private Tone tone;

        public Tone getTone() {
            return tone;
        }

        public final int getIndex() {
            return tone.getIndex();
        }

        private LibraryPatch libraryPatch;

        public final LibraryPatch getLibraryPatch() {
            return libraryPatch;
        }

        void setLibraryPatch(LibraryPatch value) {
            libraryPatch = value;
        }

        public ToneData(Tone tone) {
            this.tone = tone;
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

        Tone tone = data.getTone();

        if (tone.getToneType() != patch.getToneType())
            return false;

        data.setLibraryPatch(patch);

        SynthComponent component = tone.getComponent(SynthComponent.class);
        File presetFile = patch.getPresetFile();
        File fullPresetFile = getLibrary().getPresetFile(presetFile);
        component.loadPreset(fullPresetFile.getPath());

        return true;
    }

}
