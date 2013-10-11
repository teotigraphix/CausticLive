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
import java.util.UUID;

import com.badlogic.gdx.utils.Array;
import com.google.inject.Singleton;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.library.core.Library;
import com.teotigraphix.caustk.library.item.LibraryPatch;
import com.teotigraphix.caustk.library.item.LibraryPhrase;
import com.teotigraphix.caustk.library.item.LibraryScene;
import com.teotigraphix.caustk.project.Project;
import com.teotigraphix.caustk.sequencer.queue.QueueData;
import com.teotigraphix.libgdx.model.CaustkModelBase;

@Singleton
public class LibraryModel extends CaustkModelBase implements ILibraryModel {

    private static final String TAG = "LibraryModel";

    private static final String USER_LIBRAY_NAME = "User";

    private static final String QNAME = ILibraryModel.class.getName();

    private static final String PREF_SELECTED_SCENE_ID = QNAME + "/selectedSceneId";

    private static final String PREF_USER_LIBRARY_PATH = QNAME + "/userLibraryPath";

    //----------------------------------
    // selectedSceneId
    //----------------------------------

    private UUID selectedSceneId;

    public UUID getSelectedSceneId() {
        return selectedSceneId;
    }

    @Override
    public Array<LibraryScene> getScenes() {
        Library library = getController().getLibraryManager().getSelectedLibrary();
        List<LibraryScene> scenes = library.getScenes();
        Array<LibraryScene> result = new Array<LibraryScene>(scenes.toArray(new LibraryScene[] {}));
        return result;
    }

    public void loadScene(LibraryScene item) {
        try {
            getController().getRack().getSoundSource().createScene(item);
        } catch (CausticException e) {
            e.printStackTrace();
        }
        // set, this could be loaded from outside of the model
        selectedSceneId = item.getId();
        getController().getLogger().log(TAG, "Loaded LibraryScene " + item.toString());

        Project project = getController().getProjectManager().getProject();
        project.put(PREF_SELECTED_SCENE_ID, selectedSceneId.toString());
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public LibraryModel() {
    }

    //--------------------------------------------------------------------------
    // Overridden :: Methods 
    //--------------------------------------------------------------------------

    @Override
    public void onRegister() {

        Project project = getController().getProjectManager().getProject();
        String uid = project.getString(PREF_SELECTED_SCENE_ID, null);
        if (uid != null) {
            selectedSceneId = UUID.fromString(uid);
        }

        String path = project.getString(PREF_USER_LIBRARY_PATH, null);

        Library library = null;

        if (path == null) {

            // create the User library
            try {
                File userLibraryFile = new File(USER_LIBRAY_NAME);
                library = getController().getLibraryManager().createLibrary(userLibraryFile);
                // have to save the path without the 'libraries'
                project.put(PREF_USER_LIBRARY_PATH, userLibraryFile.getPath());
                getController().getLibraryManager().setSelectedLibrary(library);
                getController().getLibraryManager().save();
            } catch (IOException e) {
                getController().getLogger().err(TAG, "Failed to create 'User' library", e);
            }
        } else {
            // load the User library
            File directory = new File(path);
            try {
                library = getController().getLibraryManager().loadLibrary(directory);
                getController().getLibraryManager().setSelectedLibrary(library);
            } catch (IOException e) {
                getController().getLogger().err(TAG, "Failed to load '" + path + "' library", e);
            }
        }

        trigger(new OnLibraryModelLibraryChange());
    }

    //--------------------------------------------------------------------------
    // ILibraryModel API :: Methods 
    //--------------------------------------------------------------------------

    @Override
    public void restoreState() throws CausticException {
        // only load the scene if there is not a scene
        // the machine state is already loaded in the App state object
        if (selectedSceneId == null) {
            // get the first and only scene of the demo imported
            selectedSceneId = getScenes().get(0).getId();

            LibraryScene libraryScene = getController().getLibraryManager().getSelectedLibrary()
                    .findSceneById(selectedSceneId);

            if (libraryScene == null)
                throw new CausticException("Failure restoring initial LibraryScene");

            loadScene(libraryScene);
        }
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

    @Override
    public void assignTone(int toneIndex, QueueData queueData) {
        queueData.setViewChannelIndex(toneIndex);
        //trigger(new OnToneModelMachineIndexChange());
    }
}
