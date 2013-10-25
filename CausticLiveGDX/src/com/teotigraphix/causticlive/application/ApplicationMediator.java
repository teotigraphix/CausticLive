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

package com.teotigraphix.causticlive.application;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.teotigraphix.causticlive.model.CausticLiveApplicationState;
import com.teotigraphix.causticlive.model.ILibraryModel;
import com.teotigraphix.causticlive.model.ISequencerModel;
import com.teotigraphix.caustk.core.CausticException;
import com.teotigraphix.caustk.core.osc.SequencerMessage;
import com.teotigraphix.libgdx.application.ApplicationMediatorBase;
import com.teotigraphix.libgdx.application.IApplicationMediator;

@Singleton
public class ApplicationMediator extends ApplicationMediatorBase implements IApplicationMediator {

    @Inject
    ILibraryModel libraryModel;

    @Inject
    ISequencerModel sequencerModel;

    public ApplicationMediator() {
    }

    @Override
    public void onRegister() {
        super.onRegister();

        applicationModel.setStateType(CausticLiveApplicationState.class);

        // register Models
        applicationModel.registerModel(sequencerModel);
        applicationModel.registerModel(libraryModel);
    }

    @Override
    protected void onInitializeProject() {
        super.onInitializeProject();

        final String lastProject = applicationModel.getLastProject();

        getController().getRack().getTrackSequencer().setCurrentTrack(0);
        SequencerMessage.SONG_END_MODE.send(getController(), 0);

        libraryModel.reset();
        libraryModel.createFromProject(applicationModel.getProject());

        // Only import the demo song if this is the very first start of the app
        if (lastProject == null) {
            libraryModel.importDemoSong();
            try {
                libraryModel.restoreState();
            } catch (CausticException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onReloadProject() {
        super.onReloadProject();

        libraryModel.createFromProject(applicationModel.getProject());
        // load the last scene 
        try {
            libraryModel.restoreState();
        } catch (CausticException e) {
            e.printStackTrace();
        }
    }
}
