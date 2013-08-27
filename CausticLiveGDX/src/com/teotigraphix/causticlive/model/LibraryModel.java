
package com.teotigraphix.causticlive.model;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.inject.Singleton;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.library.Library;
import com.teotigraphix.caustk.library.LibraryPatch;
import com.teotigraphix.caustk.library.LibraryPhrase;
import com.teotigraphix.caustk.library.LibraryScene;
import com.teotigraphix.caustk.project.Project;
import com.teotigraphix.libgdx.model.ModelBase;

@Singleton
public class LibraryModel extends ModelBase implements ILibraryModel {

    private static final String USER_LIBRARY_PATH = "userLibraryPath";

    public LibraryModel() {
    }

    @Override
    public void onRegister() {
        Project project = getController().getProjectManager().getProject();
        String path = project.getString(USER_LIBRARY_PATH, null);

        Library library = null;

        if (path == null) {
            // create the User library
            try {
                File resource = project.getResource("libraries");
                library = getController().getLibraryManager().createLibrary(
                        new File(resource, "User"));
                project.put(USER_LIBRARY_PATH, library.getDirectory().getAbsolutePath());
                getController().getLibraryManager().setSelectedLibrary(library);
                getController().getLibraryManager().save();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // load the User library
            File directory = new File(path);
            library = getController().getLibraryManager().loadLibrary(directory);
            getController().getLibraryManager().setSelectedLibrary(library);
        }

    }

    public void importSong(File file) throws IOException, CausticException {
        Library library = getController().getLibraryManager().getSelectedLibrary();
        Library tempLibrary = getController().getLibraryManager().createLibrary();

        getController().getLibraryManager().importSong(tempLibrary, file);

        List<LibraryPatch> patches = tempLibrary.getPatches();
        List<LibraryPhrase> phrases = tempLibrary.getPhrases();
        // only one scene
        List<LibraryScene> scenes = tempLibrary.getScenes();
        LibraryScene scene = scenes.get(0);
        library.addScene(scene);

        library.getPatches().addAll(patches);
        library.getPhrases().addAll(phrases);

        getController().getLibraryManager().saveLibrary(library);
    }

    @Override
    public void onShow() {
    }

}
