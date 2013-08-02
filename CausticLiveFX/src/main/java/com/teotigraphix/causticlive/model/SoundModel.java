
package com.teotigraphix.causticlive.model;

import java.io.File;
import java.io.IOException;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.caustic.model.ModelBase;
import com.teotigraphix.caustk.application.ICaustkApplicationProvider;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.CtkDebug;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.library.LibraryScene;
import com.teotigraphix.caustk.sequencer.ISystemSequencer.SequencerMode;

@Singleton
public class SoundModel extends ModelBase {

    @Inject
    public SoundModel(ICaustkApplicationProvider provider) {
        super(provider);
    }

    @Override
    public void onRegister() {
        super.onRegister();
        CtkDebug.model(" Register: SoundModel");
        Library library = null;
        try {
            library = getController().getLibraryManager().createLibrary("Foo");
            File causticFile = new File("C:\\Users\\Work\\Documents\\caustic\\songs\\DRIVE.caustic");
            getController().getLibraryManager().importSong(library, causticFile);
            getController().getLibraryManager().setSelectedLibrary(library);
            getController().getLibraryManager().saveLibrary(library);

            // Load the scene to create the machines
            LibraryScene libraryScene = library.getScenes().get(1);
            getController().getSoundSource().createScene(libraryScene);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (CausticException e) {
            e.printStackTrace();
        }

        //        File causticFile = new File("C:\\Users\\Work\\Documents\\caustic\\songs\\ARPTEST1.caustic");
        //        try {
        //            getController().getSoundSource().loadSong(causticFile);
        //        } catch (CausticException e) {
        //            e.printStackTrace();
        //        }
        //        getController().getSystemSequencer().play(SequencerMode.PATTERN);
    }
}
