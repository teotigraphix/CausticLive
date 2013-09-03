
package com.teotigraphix.causticlive.model;

import java.util.UUID;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.SequencerMessage;
import com.teotigraphix.caustk.library.LibraryScene;
import com.teotigraphix.libgdx.model.ICaustkModelState;
import com.teotigraphix.libgdx.model.ModelBase;

@Singleton
public class SoundModel extends ModelBase implements ISoundModel {

    @Inject
    ILibraryModel libraryModel;

    @Override
    protected SoundModelState getState() {
        return (SoundModelState)super.getState();
    }

    @Override
    public void loadScene(LibraryScene item) {
        try {
            getController().getSoundSource().createScene(item);
        } catch (CausticException e) {
            e.printStackTrace();
        }
        getState().setSelectedScene(item.getId());
    }

    //----------------------------------
    // libraryScene
    //----------------------------------

    private LibraryScene libraryScene;

    @Override
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

    @Override
    protected void initalizeState() {
        super.initalizeState();

        SequencerMessage.SONG_END_MODE.send(getController(), 0);
    }

    //    public void loadScene(LibraryScene libraryScene, boolean reset) {
    //        // Load the scene to create the machines
    //        getController().getSoundSource().clearAndReset();
    //
    //        if (reset) {
    //            getState().clear();
    //        }
    //        try {
    //            getController().getSoundSource().createScene(libraryScene);
    //        } catch (CausticException e) {
    //            e.printStackTrace();
    //        }
    //
    //        if (reset) {
    //            for (Tone tone : getController().getSoundSource().getTones()) {
    //                //                ToneData data = new ToneData(tone.getIndex());
    //                //                getState().putTone(tone.getIndex(), data);
    //            }
    //        }
    //
    //        setLibraryScene(libraryScene);
    //
    //        //setSelectedTone(-1);
    //    }

    @Override
    public void onRegister() {

    }

    @Override
    public void onShow() {
        UUID uuid = getState().getSelectedScene();
        if (uuid != null) {
            LibraryScene scene = getController().getLibraryManager().getSelectedLibrary()
                    .findSceneById(uuid);
            loadScene(scene);
        }
        //        for (QueueData data : padMapModel.getPads()) {
        //            if (data.getState() == QueueDataState.SELECTED) {
        //                phrasePlayer.queue(data);
        //            }
        //            // XXX might have to reset other than selected from the saved state
        //        }
    }

    /**
     * @see SoundModel#loadScene(LibraryScene)
     * @see SoundModel#getDispatcher()
     */
    public static class OnSoundModelLibrarySceneChange {

    }

    public static class SoundModelState implements ICaustkModelState {

        @SuppressWarnings("unused")
        private transient ICaustkController controller;

        //----------------------------------
        // selectedScene
        //----------------------------------

        private UUID selectedScene;

        public UUID getSelectedScene() {
            return selectedScene;
        }

        public void setSelectedScene(UUID value) {
            selectedScene = value;
        }

        /**
         * Clears the tone map.
         */
        public void clear() {
            //toneMap.clear();
        }

        @Override
        public void sleep() {
        }

        @Override
        public void wakeup(ICaustkController controller) {
            this.controller = controller;
        }
    }

}
