////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.causticlive.model;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.badlogic.gdx.utils.Array;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.library.core.Library;
import com.teotigraphix.caustk.library.item.LibraryPatch;
import com.teotigraphix.caustk.library.item.LibraryPhrase;
import com.teotigraphix.caustk.library.item.LibraryScene;
import com.teotigraphix.caustk.project.Project;
import com.teotigraphix.libgdx.model.CaustkModelBase;

@Singleton
public class LibraryModel extends CaustkModelBase implements ILibraryModel {

    private static final String USER_LIBRARY_PATH = "userLibraryPath";

    @Override
    public Array<LibraryScene> getScenes() {
        Library library = getController().getLibraryManager().getSelectedLibrary();
        List<LibraryScene> scenes = library.getScenes();
        Array<LibraryScene> result = new Array<LibraryScene>(scenes.toArray(new LibraryScene[] {}));
        return result;
    }

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
                // XXX THis has to be project specific
                File userLib = new File("User");
                library = getController().getLibraryManager().createLibrary(userLib);
                // have to save the path without the 'libraries'
                project.put(USER_LIBRARY_PATH, userLib.getPath());
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

        //        LibraryScene libraryScene = library.getScenes().get(0);
        //        try {
        //            getController().getSoundSource().createScene(libraryScene);
        //        } catch (CausticException e) {
        //            // TODO Auto-generated catch block
        //            e.printStackTrace();
        //        }

        trigger(new OnLibraryModelLibraryChange());
    }

    @Override
    public void importSong(File file) throws IOException, CausticException {
        Library library = getController().getLibraryManager().getSelectedLibrary();
        Library tempLibrary = getController().getLibraryManager().createLibrary();
        tempLibrary.setDirectory(library.getDirectory());

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

        trigger(new OnLibraryModelLibraryChange());
    }

}
